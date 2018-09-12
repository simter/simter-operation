package tech.simter.operation.dao.reactive.mongo

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import tech.simter.operation.po.Operation

/**
 * See interfaces [ReactiveSortingRepository], [ReactiveQueryByExampleExecutor], [ReactiveCrudRepository], [ReactiveMongoOperations].
 *
 * See implements [ReactiveMongoTemplate], [SimpleReactiveMongoRepository] .
 *
 * @author RJ
 * @author zh
 */
interface OperationReactiveRepository : ReactiveCrudRepository<Operation, String> {
  fun findByCluster(cluster: String, sort: Sort): Flux<Operation>
}