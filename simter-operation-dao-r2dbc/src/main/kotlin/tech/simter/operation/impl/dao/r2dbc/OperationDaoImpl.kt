package tech.simter.operation.impl.dao.r2dbc

import io.r2dbc.client.Handle
import io.r2dbc.client.Query
import io.r2dbc.client.R2dbc
import io.r2dbc.client.Update
import io.r2dbc.spi.Clob
import io.r2dbc.spi.Row
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import tech.simter.operation.TABLE_OPERATION
import tech.simter.operation.TABLE_OPERATION_ITEM
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import java.time.OffsetDateTime
import java.util.*
import java.util.stream.Collectors

/**
 * The implementation of [OperationDao] by spring-data-r2dbc.
 *
 * Ref <https://spring.io/blog/2018/09/24/spring-data-jdbc-references-and-aggregates>
 *
 * @author RJ
 */
@Repository
class OperationDaoImpl @Autowired constructor(
  private val r2dbc: R2dbc
) : OperationDao {
  private val logger: Logger = LoggerFactory.getLogger(OperationDaoImpl::class.java)
  val insertOperationSql = """
    insert into $TABLE_OPERATION(
      id, ts, type, operator_id, operator_name,
      target_type, target_id, items_count, batch, title,
      result, remark
     ) values (
      ${'$'}1, ${'$'}2, ${'$'}3, ${'$'}4, ${'$'}5,
      ${'$'}6, ${'$'}7, ${'$'}8, ${'$'}9, ${'$'}10,
      ${'$'}11, ${'$'}12
    )
  """.trimIndent()
  val insertOperationItemSql = """
    insert into $TABLE_OPERATION_ITEM(id, pid, title, value_type, old_value, new_value)
      values (${'$'}1, ${'$'}2, ${'$'}3, ${'$'}4, ${'$'}5, ${'$'}6)
  """.trimIndent()

  @Transactional(readOnly = false)
  override fun create(operation: Operation): Mono<Void> {
    return r2dbc.useTransaction { handle ->
      logger.debug("operation={}", operation)
      logger.debug("insertOperationSql={}", insertOperationSql)
      val result = insertOperation(handle, operation)
      if (operation.items.isEmpty()) result
      else result.thenMany(insertOperationItems(handle, operation.id, operation.items))
    }
  }

  override fun get(id: String): Mono<Operation> {
    return r2dbc.inTransaction { handle ->
      handle.createQuery("select * from $TABLE_OPERATION where id = $1")
        .bind("$1", id)
        .mapRow { row -> toOperation(row) }
        .flatMap { it }
        .flatMap { operation ->
          handle.createQuery("select * from $TABLE_OPERATION_ITEM where pid = $1 order by id")
            .bind("$1", id)
            .mapRow { itemRow -> toOperationItem(itemRow) }
            .flatMap { it }
            .collectList()
            .map { operation.copy(items = it.toSet()) as Operation }
        }
    }.next()
  }

  override fun findByBatch(batch: String): Flux<Operation> {
    return r2dbc.inTransaction { handle ->
      handle.createQuery("select * from $TABLE_OPERATION where batch = $1 order by ts desc")
        .bind("$1", batch)
        .mapRow { row -> toOperation(row) }
        .flatMap { it }
        .collectMap { it.id }
        .flatMap { operations ->
          if (operations.isEmpty()) Mono.empty()
          else {
            val query = handle.createQuery("""
              select i.*
                from $TABLE_OPERATION_ITEM i
                inner join $TABLE_OPERATION p on p.id = i.pid
                where p.batch = $1 order by i.pid, i.id
              """.trimIndent()
            ).bind("$1", batch)
            mergeItems(query, operations as MutableMap<String, Operation.Impl>)
          }
        }.flatMapMany { list -> Flux.fromIterable(list.sortedByDescending { it.ts }) }
    }
  }

  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return r2dbc.inTransaction { handle ->
      handle.createQuery("select * from $TABLE_OPERATION where target_type = $1 and target_id = $2 order by ts desc")
        .bind("$1", targetType)
        .bind("$2", targetId)
        .mapRow { row -> toOperation(row) }
        .flatMap { it }
        .collectMap { it.id }
        .flatMap { operations ->
          if (operations.isEmpty()) Mono.empty()
          else {
            val query = handle.createQuery("""
              select i.*
                from $TABLE_OPERATION_ITEM i
                inner join $TABLE_OPERATION p on p.id = i.pid
                where p.target_type = $1 and p.target_id = $2 order by i.pid, i.id
              """.trimIndent()
            ).bind("$1", targetType).bind("$2", targetId)
            mergeItems(query, operations as MutableMap<String, Operation.Impl>)
          }
        }.flatMapMany { list -> Flux.fromIterable(list.sortedByDescending { it.ts }) }
    }
  }

  override fun find(
    pageNo: Int,
    pageSize: Int,
    batches: List<String>?,
    targetTypes: List<String>?,
    targetIds: List<String>?,
    search: String?
  ): Mono<Page<OperationView>> {

  private fun mergeItems(query: Query, operations: MutableMap<String, Operation.Impl>)
    : Mono<MutableCollection<Operation.Impl>> {
    return query
      .mapRow { itemRow ->
        val pid = itemRow.get("pid", String::class.java)!!
        toOperationItem(itemRow).map { Pair(pid, it) }
      }
      .flatMap { it }
      .groupBy({ it.first }, { it.second })
      .flatMap { group ->
        val pid = group.key() as String
        group.collectList().doOnNext {
          // rebuild with items
          operations[pid] = operations[pid]!!.copy(items = it.toSet())
        }
      }.then(operations.values.toMono())
  }

  internal fun insertOperationItems(handle: Handle, operationId: String, items: Set<Operation.Item>): Flux<Int> {
    val insert = handle.createUpdate(insertOperationItemSql)
    items.forEach { item ->
      var i = 0

      // $1 to $6
      insert.bind("\$${++i}", item.id)
      insert.bind("\$${++i}", operationId)
      bindValue(insert, "\$${++i}", item.title, String::class.java)
      insert.bind("\$${++i}", item.valueType)
      bindValue(insert, "\$${++i}", item.oldValue, String::class.java)
      bindValue(insert, "\$${++i}", item.newValue, String::class.java)

      // add to batch
      insert.add()
    }

    // execute sql
    return insert.execute()
  }

  internal fun insertOperation(handle: Handle, operation: Operation): Mono<Int> {
    val insert = handle.createUpdate(insertOperationSql)
    var i = 0

    // $1 to $5
    insert.bind("\$${++i}", operation.id)
    insert.bind("\$${++i}", operation.ts)
    insert.bind("\$${++i}", operation.type)
    insert.bind("\$${++i}", operation.operatorId)
    insert.bind("\$${++i}", operation.operatorName)

    // $6 to $10
    insert.bind("\$${++i}", operation.targetType)
    insert.bind("\$${++i}", operation.targetId)
    insert.bind("\$${++i}", operation.items.size)
    bindValue(insert, "\$${++i}", operation.batch, String::class.java)
    bindValue(insert, "\$${++i}", operation.title, String::class.java)

    // $11 to $12
    bindValue(insert, "\$${++i}", operation.result, String::class.java)
    bindValue(insert, "\$${++i}", operation.remark, String::class.java)

    // execute sql
    return insert.execute().next()
  }

  private fun bindValue(update: Update, identifier: String, value: Any?, valueType: Class<*>) {
    if (value == null) update.bindNull(identifier, valueType)
    else update.bind(identifier, value)
  }

  private fun toOperation(operationRow: Row): Mono<Operation.Impl> {
    // need to early decode non-clob data because postgres would release row after call `PostgresqlResult.map`.
    // otherwise r2dbc-postgresql throw `IllegalStateException: Value cannot be retrieved after row has been released`.
    // r2dbc-h2 without this problem.
    val withoutClob = Operation.Impl(
      id = operationRow.get("id", String::class.java)!!,
      ts = operationRow.get("ts", OffsetDateTime::class.java)!!,
      type = operationRow.get("type", String::class.java)!!,
      operatorId = operationRow.get("operator_id", String::class.java)!!,
      operatorName = operationRow.get("operator_name", String::class.java)!!,
      targetId = operationRow.get("target_id", String::class.java)!!,
      targetType = operationRow.get("target_type", String::class.java)!!,
      title = operationRow.get("title", String::class.java),
      result = operationRow.get("result", String::class.java),
      batch = operationRow.get("batch", String::class.java)
    )

    // decode clob data
    val remarkClob = operationRow.get("remark", Clob::class.java)
      ?.stream()?.toFlux()?.collect(Collectors.joining())?.map { Optional.of(it) }
      ?: Optional.empty<String>().toMono()

    // combine
    return remarkClob.map { withoutClob.copy(remark = it.orElse(null)) }
  }

  private fun toOperationItem(itemRow: Row): Mono<Operation.Item.Impl> {
    // need to early decode non-clob data because postgres would release row after call `PostgresqlResult.map`.
    // otherwise r2dbc-postgresql throw `IllegalStateException: Value cannot be retrieved after row has been released`.
    // r2dbc-h2 without this problem.
    val withoutClob = Operation.Item.Impl(
      id = itemRow.get("id", String::class.java)!!,
      title = itemRow.get("title", String::class.java),
      valueType = itemRow.get("value_type", String::class.java)!!
    )

    // decode clob data
    val oldValueClob = itemRow.get("old_value", Clob::class.java)
      ?.stream()?.toFlux()?.collect(Collectors.joining())?.map { Optional.of(it) }
      ?: Optional.empty<String>().toMono()
    val newValueClob = itemRow.get("new_value", Clob::class.java)
      ?.stream()?.toFlux()?.collect(Collectors.joining())?.map { Optional.of(it) }
      ?: Optional.empty<String>().toMono()

    // combine
    return Mono.zip(oldValueClob, newValueClob).map {
      withoutClob.copy(
        oldValue = it.t1.orElse(null),
        newValue = it.t2.orElse(null)
      )
    }
  }
}