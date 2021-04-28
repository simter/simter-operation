package tech.simter.operation.impl.dao.r2dbc

import io.r2dbc.spi.Row
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.by
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.kotlin.data.Page
import tech.simter.operation.TABLE_OPERATION
import tech.simter.operation.TABLE_OPERATION_ITEM
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationView
import tech.simter.r2dbc.kotlin.bindNullable
import java.time.OffsetDateTime

/**
 * The implementation of [OperationDao] by spring-data-r2dbc.
 *
 * Ref <https://spring.io/blog/2018/09/24/spring-data-jdbc-references-and-aggregates>
 *
 * @author RJ
 */
@Repository
class OperationDaoImpl @Autowired constructor(
  private val databaseClient: DatabaseClient,
  private val entityOperations: R2dbcEntityOperations
) : OperationDao {
  private val insertOperationSql = """
    insert into $TABLE_OPERATION(
      id, ts, type, operator_id, operator_name,
      target_type, target_id, items_count, batch, title,
      result, remark
     ) values (
      :id, :ts, :type, :operatorId, :operatorName,
      :targetType, :targetId, :itemsCount, :batch, :title,
      :result, :remark
    )
  """.trimIndent()

  private fun createInsertSql4OperationItem(count: Int): String {
    val values = (0 until count).joinToString(", \r\n") { "(:pid, :id$it, :title$it, :valueType$it, :oldValue$it, :newValue$it)" }
    return "insert into $TABLE_OPERATION_ITEM(pid, id, title, value_type, old_value, new_value)\r\n  values $values"
  }

  @Transactional(readOnly = false)
  override fun create(operation: Operation): Mono<Void> {
    val result = databaseClient
      // insert to main table for operation
      .sql(insertOperationSql)
      .bind("id", operation.id)
      .bind("ts", operation.ts)
      .bind("type", operation.type)
      .bind("operatorId", operation.operatorId)
      .bind("operatorName", operation.operatorName)
      .bind("targetType", operation.targetType)
      .bind("targetId", operation.targetId)
      .bind("itemsCount", operation.items.size)
      .bindNullable<String>("title", operation.title)
      .bindNullable<String>("result", operation.result)
      .bindNullable<String>("remark", operation.remark)
      .bindNullable<String>("batch", operation.batch)
      .then()

    // insert to sub table for items
    return if (operation.items.isEmpty()) result
    else {
      var spec = databaseClient.sql(createInsertSql4OperationItem(operation.items.size))
        .bind("pid", operation.id)
      operation.items.forEachIndexed { index, item ->
        spec = spec
          .bind("id$index", item.id)
          .bind("valueType$index", item.valueType)
          .bindNullable<String>("title$index", item.title)
          .bindNullable<String>("oldValue$index", item.oldValue)
          .bindNullable<String>("newValue$index", item.newValue)
      }

      result.then(spec.then())
    }
  }

  @Transactional(readOnly = true)
  override fun get(id: String): Mono<Operation> {
    return entityOperations.select(Operation.Item.Impl::class.java)
      .from(TABLE_OPERATION_ITEM)
      .matching(query(where("pid").`is`(id)).sort(by(ASC, "id")))
      .all()
      .collectList()
      .flatMap { items ->
        databaseClient.sql("""
          select id, ts, type, operator_id, operator_name, target_type, target_id, 
            batch, title, result, remark
          from $TABLE_OPERATION where id = :id""".trimIndent()
        ).bind("id", id)
          .map { row: Row -> rowMapper4Operation(row, items) }
          .one()
      }
  }

  @Transactional(readOnly = true)
  override fun findByBatch(batch: String): Flux<Operation> {
    return databaseClient.sql("""
      select i.*
      from $TABLE_OPERATION_ITEM i
      inner join $TABLE_OPERATION o on o.id = i.pid
      where o.batch = :batch
      order by i.pid, i.id""".trimIndent()
    ).bind("batch", batch)
      .map { row: Row -> rowMapper4OperationItem(row) }
      .all()
      .collectMultimap({ it.first }, { it.second })
      .flatMapMany { items ->
        databaseClient.sql("""
          select o.*
          from $TABLE_OPERATION o
          where o.batch = :batch
          order by o.ts desc""".trimIndent()
        ).bind("batch", batch)
          .map { row: Row -> rowMapper4Operation(row, items[row.get("id", String::class.java)!!]) }
          .all()
      }
  }

  @Transactional(readOnly = true)
  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return databaseClient.sql("""
      select i.*
      from $TABLE_OPERATION_ITEM i
      inner join $TABLE_OPERATION o on o.id = i.pid
      where o.target_type = :targetType
      and o.target_id = :targetId
      order by i.pid, i.id""".trimIndent()
    ).bind("targetType", targetType)
      .bind("targetId", targetId)
      .map { row: Row -> rowMapper4OperationItem(row) }
      .all()
      .collectMultimap({ it.first }, { it.second })
      .flatMapMany { items ->
        databaseClient.sql("""
          select o.*
          from $TABLE_OPERATION o
          where o.target_type = :targetType
          and o.target_id = :targetId
          order by o.ts desc""".trimIndent()
        ).bind("targetType", targetType)
          .bind("targetId", targetId)
          .map { row: Row -> rowMapper4Operation(row, items[row.get("id", String::class.java)!!]) }
          .all()
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
    // create common query
    val query = entityOperations.select(OperationView.Impl::class.java).from(TABLE_OPERATION)

    // create query condition
    var condition = Criteria.empty()
    if (!batches.isNullOrEmpty()) condition = condition.and("batch").`in`(batches)
    if (!targetTypes.isNullOrEmpty()) condition = condition.and("targetType").`in`(targetTypes)
    if (!targetIds.isNullOrEmpty()) condition = condition.and("targetId").`in`(targetIds)
    search?.let {
      val searchStr = if (it.contains("%")) it else "%$it%"
      condition = condition.and(
        where("title").like(searchStr)
          .or("batch").like(searchStr)
          .or("targetType").like(searchStr)
          .or("operatorName").like(searchStr)
      )
    }

    // do page query
    return query.matching(query(condition)).count() // query total count
      .flatMap { totalCount ->
        val offset = Page.calculateOffset(pageNo, pageSize)
        val sort = by(DESC, "ts")
        if (totalCount <= 0) Mono.just(Page.of(
          limit = pageSize,
          offset = offset,
          total = 0,
          rows = emptyList()
        ))
        else {
          // query real rows
          query.matching(
            query(condition).sort(sort).limit(pageSize).offset(offset)
          ).all()
            .collectList()
            .map { rows ->
              Page.of(
                limit = pageSize,
                offset = offset,
                total = totalCount,
                rows = rows as List<OperationView>
              )
            }
        }
      }
  }

  private fun rowMapper4Operation(row: Row, items: Collection<Operation.Item>?): Operation {
    return Operation.of(
      id = row.get("id", String::class.java)!!,
      ts = row.get("ts", OffsetDateTime::class.java)!!,
      type = row.get("type", String::class.java)!!,
      operatorId = row.get("operator_id", String::class.java)!!,
      operatorName = row.get("operator_name", String::class.java)!!,
      targetId = row.get("target_id", String::class.java)!!,
      targetType = row.get("target_type", String::class.java)!!,
      title = row.get("title", String::class.java),
      remark = row.get("remark", String::class.java),
      result = row.get("result", String::class.java),
      batch = row.get("batch", String::class.java),
      items = items?.toSet() ?: emptySet()
    )
  }

  private fun rowMapper4OperationItem(row: Row): Pair<String, Operation.Item> {
    return Pair(
      row.get("pid", String::class.java)!!,
      Operation.Item.of(
        id = row.get("id", String::class.java)!!,
        valueType = row.get("value_type", String::class.java)!!,
        title = row.get("title", String::class.java),
        oldValue = row.get("old_value", String::class.java),
        newValue = row.get("new_value", String::class.java)
      )
    )
  }
}