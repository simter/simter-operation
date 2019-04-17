package tech.simter.operation.rest.webflux.handler.operation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.operation.po.Operation
import tech.simter.operation.service.OperationService

/**
 * The [HandlerFunction] for fins [Operation]s by cluster
 *
 * @author zh
 * @author RJ
 */
@Component
class FindByClusterHandler @Autowired constructor(
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return ok().contentType(APPLICATION_JSON_UTF8)
      .body(operationService.findByCluster(request.pathVariable("cluster")))
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = GET("/cluster/{cluster}")
  }
}