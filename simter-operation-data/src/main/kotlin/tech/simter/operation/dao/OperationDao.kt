package tech.simter.operation.dao

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.po.Operation
import tech.simter.operation.service.OperationService

/**
 * The Dao Interface.
 *
 * This interface should only be use by [OperationService]. It is design to public just for multiple Dao implements.
 *
 * @author RJ
 */
interface OperationDao {
  /**
   * Find the specific [id] [Operation] instance.
   *
   * Return [Mono.empty] if not exists.
   */
  fun get(id: String): Mono<Operation>

  /**
   * Find all [Operation]s with the specific [cluster].
   *
   * Return [Operation]s or a empty flux without data if none found
   */
  fun findByCluster(cluster: String): Flux<Operation>

  /**
   * Create one [Operation].
   *
   * @return [Mono] signaling when operation has completed
   */
  fun create(operation: Operation): Mono<Void>

  /**
   * Batch save some [Operation].
   *
   * @return [Mono] signaling when operations has completed
   */
  fun saveAll(operations: List<Operation>): Mono<Void>
}