package tech.simter.operation.rest.webflux.handler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.exception.PermissionDeniedException
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService
import tech.simter.reactive.web.Utils.responseForbiddenStatus

/**
 * The [HandlerFunction] for find [Operation]s by batch
 *
 * @author zh
 * @author RJ
 */
@Component
class FindByBatchHandler @Autowired constructor(
  private val json: Json,
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return operationService.findByBatch(request.pathVariable("batch"))
      .collectList()
      .flatMap {
        if (it.isEmpty()) noContent().build()
        else ok().contentType(APPLICATION_JSON).bodyValue(json.encodeToString(it))
      }
      .onErrorResume(PermissionDeniedException::class.java, ::responseForbiddenStatus)
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = GET("/batch/{batch}")
  }
}