package tech.simter.operation.rest.webflux.handler.operation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.operation.service.OperationService

/**
 * FindByCluster's [HandlerFunction]。
 *
 * @author zh
 */
class FindByClusterHandler @Autowired constructor(
  private val operationService: OperationService
) {
  fun findByCluster(request: ServerRequest): Mono<ServerResponse> {
    return ok().contentType(APPLICATION_JSON_UTF8)
      .body(operationService.findByCluster(request.pathVariable("cluster")))
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = GET("/cluster/{cluster}")
  }
}