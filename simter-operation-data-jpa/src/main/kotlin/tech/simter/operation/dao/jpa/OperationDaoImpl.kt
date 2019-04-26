package tech.simter.operation.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation
import tech.simter.reactive.jpa.ReactiveJpaWrapper

/**
 * The JPA implementation of [OperationDao].
 *
 * @author RJ
 * @author zh
 */
@Component
class OperationDaoImpl @Autowired constructor(
  private val blockDao: OperationBlockDao,
  private val wrapper: ReactiveJpaWrapper
) : OperationDao {
  override fun get(id: String): Mono<Operation> {
    return wrapper.fromCallable { blockDao.get(id) }
      .flatMap { if (it.isPresent) Mono.just(it.get()) else Mono.empty() }
  }

  override fun findByCluster(cluster: String): Flux<Operation> {
    return wrapper.fromIterable { blockDao.findByCluster(cluster) }
  }

  override fun create(vararg operations: Operation): Mono<Void> {
    blockDao.create(*operations)
    return Mono.empty()
  }
}