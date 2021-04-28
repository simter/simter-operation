package tech.simter.operation.rest.webflux.handler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono
import tech.simter.exception.PermissionDeniedException
import tech.simter.kotlin.data.Page
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService

/**
 * The [HandlerFunction] for find [Operation]s by page
 *
 * @author xz
 * @author RJ
 */
@Component
class FindHandler @Autowired constructor(
  @Value("\${simter.default-page-limit: ${Page.DEFAULT_LIMIT}}")
  private val defaultPageLimit: Int,
  private val json: Json,
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return operationService.find(
      request.queryParam("page-no").orElse("1").toInt(),
      request.queryParam("page-size").orElse(defaultPageLimit.toString()).toInt(),
      request.queryParams()["batch"],
      request.queryParams()["target-type"],
      request.queryParams()["target-id"],
      request.queryParam("search").orElse(null)
    )
      // TODO delete json.encodeToString when spring-boot support auto config
      // response
      .flatMap { ok().contentType(APPLICATION_JSON).bodyValue(json.encodeToString(Page.toMap(it, json))) }
      // error mapping
      .onErrorResume(PermissionDeniedException::class.java) {
        if (it.message.isNullOrBlank()) status(FORBIDDEN).build()
        else status(FORBIDDEN).contentType(TEXT_PLAIN).bodyValue(it.message ?: "")
      }
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = GET("/")
  }
}