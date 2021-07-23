package tech.simter.operation.impl.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.kotlin.data.Page
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationView
import tech.simter.reactive.context.SystemContext
import tech.simter.reactive.jpa.ReactiveJpaWrapper
import tech.simter.reactive.security.ReactiveSecurityService

/**
 * The JPA implementation of [OperationDao].
 *
 * @author RJ
 * @author zh
 */
@Repository
class OperationDaoImpl @Autowired constructor(
  private val securityService: ReactiveSecurityService,
  private val blockDao: OperationBlockDao,
  private val wrapper: ReactiveJpaWrapper
) : OperationDao {
  override fun create(operation: Operation): Mono<Void> {
    return wrapper.fromRunnable { blockDao.create(operation) }
  }

  override fun create(
    type: String,
    targetType: String,
    targetId: String,
    title: String?,
    batch: String?,
    items: Set<Operation.Item>,
    remark: String?,
    result: String?
  ): Mono<Void> {
    return securityService.getAuthenticatedUser()
      .map { it.orElseGet { SystemContext.User(id = 0, account = "UNKNOWN", name = "UNKNOWN") } } // get context user info
      .flatMap { user ->
        this.create(Operation.of(
          type = type,
          operatorId = user.id.toString(),
          operatorName = user.name,
          targetType = targetType,
          targetId = targetId,
          title = title,
          batch = batch,
          items = items,
          remark = remark,
          result = result
        ))
      }
  }

  override fun get(id: String): Mono<Operation> {
    return wrapper.fromCallable { blockDao.get(id) }
      .flatMap { if (it.isPresent) Mono.just(it.get()) else Mono.empty() }
  }

  override fun findByBatch(batch: String): Flux<Operation> {
    return wrapper.fromIterable { blockDao.findByBatch(batch) }
  }

  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return wrapper.fromIterable { blockDao.findByTarget(targetType, targetId) }
  }

  override fun find(
    pageNo: Int,
    pageSize: Int,
    batches: List<String>?,
    targetTypes: List<String>?,
    targetIds: List<String>?,
    search: String?
  ): Mono<Page<OperationView>> {
    return wrapper.fromCallable { blockDao.find(pageNo, pageSize, batches, targetTypes, targetIds, search) }
  }
}