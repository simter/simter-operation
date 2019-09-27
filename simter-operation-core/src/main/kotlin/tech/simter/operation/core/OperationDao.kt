package tech.simter.operation.core

import org.springframework.data.domain.Page
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * The Dao Interface.
 *
 * @author RJ
 */
interface OperationDao {
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

  /**
   * Find [Page<Operation>] with the specific [batches], [targetTypes] and [targetIds].
   * And fuzzy search with [Operation.title], [Operation.operatorName], [Operation.batch] and [Operation.targetType].
   *
   * Return [Page<Operation>] order by [Operation.ts] desc.
   */
  fun find(
    pageNo: Int = 1,
    pageSize: Int = 25,
    batches: List<String>? = null,
    targetTypes: List<String>? = null,
    targetIds: List<String>? = null,
    search: String? = null
  ): Mono<Page<Operation>>
}