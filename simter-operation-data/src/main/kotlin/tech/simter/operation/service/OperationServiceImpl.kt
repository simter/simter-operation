package tech.simter.operation.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
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
@Transactional
class OperationServiceImpl @Autowired constructor(
  private val dao: OperationDao
) : OperationService {
  override fun get(id: String): Mono<Operation> {
    return dao.get(id)
  }

  override fun findByCluster(cluster: String): Flux<Operation> {
    return dao.findByCluster(cluster)
  }
}