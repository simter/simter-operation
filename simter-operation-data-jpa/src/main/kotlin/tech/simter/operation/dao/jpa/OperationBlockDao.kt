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
   * Find the specific [id] [Operation] instance.
   *
   * Return [Optional.empty] if not exists.
   */
  fun get(id: String): Optional<Operation>

  /**
   * Find all [Operation]s with the specific [cluster].
   *
   * Return [Operation]s or a empty list
   */
  fun findByCluster(cluster: String): List<Operation>

  /**
   * Create one or some [Operation].
   */
  fun create(vararg operations: Operation)
}