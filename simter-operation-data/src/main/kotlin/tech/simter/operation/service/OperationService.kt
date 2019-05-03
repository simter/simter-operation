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
   * Create one [Operation].
   *
   * @return [Mono] signaling when operation created
   */
  fun create(operation: Operation): Mono<Void>

  /**
   * Find the specific [id] [Operation] instance.
   *
   * Return [Mono.empty] if not exists.
   */
  fun get(id: String): Mono<Operation>

  /**
   * Find all [Operation]s with the specific [batch].
   *
   * Return [Operation]s order by [Operation.ts] desc or [Flux.empty] if found nothing
   */
  fun findByBatch(batch: String): Flux<Operation>

  /**
   * Find all [Operation]s with the specific [targetType] and [targetId].
   *
   * Return [Operation]s order by [Operation.ts] desc or [Flux.empty] if found nothing
   */
  fun findByTarget(targetType: String, targetId: String): Flux<Operation>
}