package tech.simter.operation.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.OPERATION_CREATE
import tech.simter.operation.OPERATION_READ
import tech.simter.operation.PACKAGE
import tech.simter.operation.core.Operation
import tech.simter.operation.core.Operation.Item
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationService
import tech.simter.operation.impl.ImmutableOperation
import tech.simter.operation.impl.ImmutableOperation.ImmutableItem
import tech.simter.reactive.context.SystemContext
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.reactive.security.ReactiveSecurityService

/**
 * The Service implementation of [OperationService].
 *
 * @author RJ
 */
@Service
class OperationServiceImpl @Autowired constructor(
  @Qualifier("$PACKAGE.ModuleAuthorizer")
  private val moduleAuthorizer: ModuleAuthorizer,
  private val securityService: ReactiveSecurityService,
  private val dao: OperationDao
) : OperationService {
  override fun create(operation: Operation): Mono<Void> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_CREATE).then(
      dao.create(operation)
    )
  }

  override fun create(
    type: String,
    targetType: String,
    targetId: String,
    title: String?,
    batch: String?,
    items: Set<Item>,
    remark: String?,
    result: String?
  ): Mono<Void> {
    return securityService.getAuthenticatedUser()
      .map { it.orElseGet { SystemContext.User(id = 0, account = "UNKNOWN", name = "UNKNOWN") } } // get context user info
      .flatMap { user ->
        this.create(ImmutableOperation(
          type = type,
          operatorId = user.id.toString(),
          operatorName = user.name,
          targetType = targetType,
          targetId = targetId,
          title = title,
          batch = batch,
          items = items.map { ImmutableItem.from(it) }.toSet(),
          remark = remark,
          result = result
        ))
      }
  }

  override fun get(id: String): Mono<Operation> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).then(
      dao.get(id)
    )
  }

  override fun findByBatch(batch: String): Flux<Operation> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findByBatch(batch)
    )
  }

  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findByTarget(targetType, targetId)
    )
  }

  override fun find(targetTypes: List<String>?, pageNo: Int, pageSize: Int, search: String?): Mono<Page<Operation>> {
    TODO("not implemented")
  }
}