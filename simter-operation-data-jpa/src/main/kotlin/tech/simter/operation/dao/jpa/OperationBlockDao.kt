package tech.simter.operation.dao.jpa

import tech.simter.operation.po.Operation
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
}