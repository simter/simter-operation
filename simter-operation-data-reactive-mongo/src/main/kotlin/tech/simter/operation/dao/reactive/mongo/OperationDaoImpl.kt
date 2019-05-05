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

  override fun create(operation: Operation): Mono<Void> {
    return repository.save(operation).then(Mono.empty())
  }

  override fun get(id: String): Mono<Operation> {
    return repository.findById(id)
  }

  override fun findByBatch(batch: String): Flux<Operation> {
    return repository.findByBatch(batch, Sort(Sort.Direction.DESC, "ts"))
  }

  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return repository.findByTargetTypeAndTargetId(targetType, targetId, Sort(Sort.Direction.DESC, "ts"))
  }
}