package tech.simter.operation.service

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
}