package tech.simter.operation.rest.webflux.handler

import kotlinx.serialization.SerializationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService
import tech.simter.reactive.web.Utils.responseBadRequestStatus

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
    return request.bodyToMono<Operation.Impl>()
      .flatMap { operationService.create(it) }
      .then(noContent().build())
      .onErrorResume(SerializationException::class.java, ::responseBadRequestStatus)
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = POST("/").and(contentType(APPLICATION_JSON))
  }
}