package tech.simter.operation.rest.webflux.handler


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService
import tech.simter.operation.impl.ImmutableOperation

/**
 * The [HandlerFunction] for create one [Operation]
 *
 * @author zh
 * @author RJ
 */
@Component
class CreateHandler @Autowired constructor(
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return request.bodyToMono<ImmutableOperation>()
      .flatMap { operationService.create(it) }
      .then(noContent().build())
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = POST("/").and(contentType(APPLICATION_JSON_UTF8))
  }
}