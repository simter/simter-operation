package tech.simter.operation.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.OPERATION_CREATE
import tech.simter.operation.OPERATION_READ
import tech.simter.operation.PACKAGE
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation
import tech.simter.operation.po.OperationItem
import tech.simter.reactive.context.SystemContext
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.reactive.security.ReactiveSecurityService

/**
 * The Service implementation of [OperationService].
 *
 * @author RJ
 */
@Component
class OperationServiceImpl @Autowired constructor(
  @Qualifier("$PACKAGE.service.ModuleAuthorizer")
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
    title: String,
    batch: String?,
    items: Set<OperationItem>?
  ): Mono<Void> {
    return securityService.getAuthenticatedUser()
      .map { it.orElseGet { SystemContext.User(id = 0, account = "UNKNOWN", name = "UNKNOWN") } } // get context user info
      .flatMap { user ->
        val operation = Operation(
          type = type,
          operatorId = user.id.toString(),
          operatorName = user.name,
          targetType = targetType,
          targetId = targetId,
          title = title,
          batch = batch
        )
        items?.forEach { operation.addItem(it) }
        this.create(operation)
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
}