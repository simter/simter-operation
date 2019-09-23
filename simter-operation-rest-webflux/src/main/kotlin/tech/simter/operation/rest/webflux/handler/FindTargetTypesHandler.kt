package tech.simter.operation.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.operation.core.OperationService

/**
 * The [HandlerFunction] for find all target type
 *
 * @author xz
 */
@Component
class FindTargetTypesHandler @Autowired constructor(
  private val service: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val flux = service.findTargetTypes()
    return flux.hasElements().flatMap {
      if (it)
        ok().contentType(APPLICATION_JSON).body(flux.collectList())
      else
        noContent().build()
    }
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = RequestPredicates.GET("/target-type")
  }
}