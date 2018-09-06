package tech.simter.operation.dao

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
}