package tech.simter.operation.impl.dao.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.mongo.po.OperationPo

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
    return repository.save(OperationPo.from(operation)).then(Mono.empty())
  }

  override fun get(id: String): Mono<Operation> {
    return repository.findById(id).map { it as Operation }
  }

  override fun findByBatch(batch: String): Flux<Operation> {
    return repository.findByBatch(batch, Sort(Sort.Direction.DESC, "ts"))
      .map { it as Operation }
  }

  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return repository.findByTargetTypeAndTargetId(targetType, targetId, Sort(Sort.Direction.DESC, "ts"))
      .map { it as Operation }
  }
}