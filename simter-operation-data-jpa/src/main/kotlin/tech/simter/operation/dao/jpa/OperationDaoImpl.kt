package tech.simter.operation.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation

/**
 * The JPA implementation of [OperationDao].
 *
 * @author RJ
 */
@Component
@Transactional
class OperationDaoImpl @Autowired constructor(
  private val repository: OperationJpaRepository
) : OperationDao {
  override fun get(id: String): Mono<Operation> {
    return Mono.justOrEmpty(repository.findById(id))
  }

  override fun findByCluster(cluster: String): Flux<Operation> {
    TODO("not implemented")
  }

  override fun create(operation: Operation): Mono<Void> {
    TODO("not implemented")
  }
}