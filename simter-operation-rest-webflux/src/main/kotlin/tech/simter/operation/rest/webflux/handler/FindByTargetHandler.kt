package tech.simter.operation.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService

/**
 * The [HandlerFunction] for find [Operation]s by target
 *
 * @author RJ
 */
@Component
class FindByTargetHandler @Autowired constructor(
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val flux = operationService.findByTarget(
      request.pathVariable("targetType"),
      request.pathVariable("targetId")
    )
    return flux.hasElements()
      .flatMap {
        if (it) ok().contentType(APPLICATION_JSON).body(flux)
        else noContent().build()
      }
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = GET("/target/{targetType}/{targetId}")
  }
}