package tech.simter.operation.impl.dao.mongo

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import tech.simter.operation.impl.dao.mongo.po.OperationPo

/**
 * See interfaces [ReactiveSortingRepository], [ReactiveQueryByExampleExecutor], [ReactiveCrudRepository], [ReactiveMongoOperations].
 *
 * See implements [ReactiveMongoTemplate], [SimpleReactiveMongoRepository] .
 *
 * @author RJ
 * @author zh
 */
interface OperationReactiveRepository : ReactiveCrudRepository<OperationPo, String> {
  fun findByBatch(batch: String, sort: Sort): Flux<OperationPo>
  fun findByTargetTypeAndTargetId(targetType: String, targetId: String, sort: Sort): Flux<OperationPo>
}