package tech.simter.operation.rest.webflux.handler.operation


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import reactor.core.publisher.Mono
import tech.simter.operation.po.Operation
import tech.simter.operation.service.OperationService

/**
 * CreateOperation's [HandlerFunction]。
 *
 * @author zh
 */
class CreateHandler @Autowired constructor(
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return request.bodyToMono<Operation>()
      .flatMap { operationService.create(it) }
      .then(noContent().build())
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = POST("/")
  }
}