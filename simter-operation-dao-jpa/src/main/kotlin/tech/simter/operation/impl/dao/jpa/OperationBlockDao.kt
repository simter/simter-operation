package tech.simter.operation.impl.dao.jpa

import org.springframework.data.domain.Page
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import java.util.*

/**
 * The [Operation] block Dao Interface.
 *
 * @author RJ
 */
interface OperationBlockDao {
  /**
   * Create one [Operation].
   */
  fun create(operation: Operation)

  /**
   * Find the specific [id] [Operation] instance.
   *
   * Return [Optional.empty] if not exists.
   */
  fun get(id: String): Optional<Operation>

  /**
   * Find all [Operation]s with the specific [batch].
   *
   * Return [Operation]s or a empty list
   */
  fun findByBatch(batch: String): List<Operation>

  /**
   * Find all [Operation]s with the specific [targetType] and [targetId].
   *
   * Return [Operation]s or a empty list
   */
  fun findByTarget(targetType: String, targetId: String): List<Operation>

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
  ): Page<Operation>

  /**
   * Find all targetType.
   *
   * Return all targetType or a empty list.
   */
  fun findTargetTypes(): List<String>
}