package tech.simter.operation.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation

/**
 * The Service implementation of [OperationService].
 *
 * @author RJ
 */
@Component
class OperationServiceImpl @Autowired constructor(
  private val dao: OperationDao
) : OperationService {
  override fun get(id: String): Mono<Operation> {
    return dao.get(id)
  }

  override fun findByCluster(cluster: String): Flux<Operation> {
    return dao.findByCluster(cluster)
  }

  override fun create(operation: Operation): Mono<Void> {
    return dao.create(operation)
  }
}