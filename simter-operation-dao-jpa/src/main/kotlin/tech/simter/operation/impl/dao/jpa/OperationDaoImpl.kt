package tech.simter.operation.impl.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.reactive.jpa.ReactiveJpaWrapper

/**
 * The JPA implementation of [OperationDao].
 *
 * @author RJ
 * @author zh
 */
@Repository
class OperationDaoImpl @Autowired constructor(
  private val blockDao: OperationBlockDao,
  private val wrapper: ReactiveJpaWrapper
) : OperationDao {
  override fun create(operation: Operation): Mono<Void> {
    return wrapper.fromRunnable { blockDao.create(operation) }
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
}