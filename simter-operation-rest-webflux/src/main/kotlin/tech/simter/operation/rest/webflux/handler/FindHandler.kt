package tech.simter.operation.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.exception.PermissionDeniedException
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService
import tech.simter.operation.rest.webflux.convert

/**
 * The [HandlerFunction] for find [Operation]s by page
 *
 * @author xz
 */
@Component
class FindHandler @Autowired constructor(
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return operationService.find(
      request.queryParam("page-no").orElse("1").toInt(),
      request.queryParam("page-size").orElse("25").toInt(),
      request.queryParams()["batch"],
      request.queryParams()["target-type"],
      request.queryParams()["target-id"],
      request.queryParam("search").orElse(null)
    ).map { it.convert() }
      // response
      .flatMap { ok().contentType(APPLICATION_JSON).syncBody(it) }
      // error mapping
      .onErrorResume(PermissionDeniedException::class.java) {
        if (it.message.isNullOrBlank()) status(FORBIDDEN).build()
        else status(FORBIDDEN).contentType(TEXT_PLAIN).syncBody(it.message ?: "")
      }
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = GET("/")
  }
}