package tech.simter.operation.core

import org.springframework.data.domain.Page
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation.Item

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
   * Create one [Operation].
   *
   * - auto set [Operation.ts] to current timestamp.
   * - auto set [Operation.operatorId] and [Operation.operatorName] from context.
   *
   * @return [Mono] signaling when operation created
   */
  fun create(
    type: String,
    targetType: String,
    targetId: String,
    title: String? = null,
    batch: String? = null,
    items: Set<Item> = emptySet(),
    remark: String? = null,
    result: String? = null
  ): Mono<Void>

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