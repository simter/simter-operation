package tech.simter.operation.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.po.Operation

/**
 * The Service Interface.
 *
 * This interface is design for all external modules to use.
 *
 * @author RJ
 */
interface OperationService {
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
}