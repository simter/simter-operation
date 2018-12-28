package tech.simter.operation.dao.reactive.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation

/**
 * The Reactive MongoDB implementation of [OperationDao].
 *
 * @author RJ
 * @author zh
 */
@Component
class OperationDaoImpl @Autowired constructor(
  private val repository: OperationReactiveRepository
) : OperationDao {
  override fun get(id: String): Mono<Operation> {
    return repository.findById(id)
  }

  override fun findByCluster(cluster: String): Flux<Operation> {
    return repository.findByCluster(cluster, Sort(Sort.Direction.DESC, "time"))
  }

  override fun create(vararg operations: Operation): Mono<Void> {
    return repository.saveAll(operations.toList())
      .then(Mono.empty())
  }
}