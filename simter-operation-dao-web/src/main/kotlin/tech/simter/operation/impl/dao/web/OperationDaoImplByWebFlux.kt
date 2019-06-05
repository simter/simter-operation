package tech.simter.operation.impl.dao.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.ImmutableOperation
import tech.simter.reactive.web.Utils.createWebClient

/**
 * The implementation of [OperationDao] by WebFlux.
 *
 * @author RJ
 */
@Component
class OperationDaoImplByWebFlux(
  @Value("\${module.server-address.simter-operation:http://localhost:9014/operation}")
  private val serverAddress: String,
  @Value("\${proxy.host:#{null}}") private val proxyHost: String?,
  @Value("\${proxy.port:#{null}}") private val proxyPort: Int?
) : OperationDao {
  //private val logger: Logger = LoggerFactory.getLogger(OperationDaoImplByWebFlux::class.java)
  private val client: WebClient = createWebClient(
    baseUrl = serverAddress,
    proxyHost = proxyHost,
    proxyPort = proxyPort
  )

  override fun create(operation: Operation): Mono<Void> {
    return client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .syncBody(operation)
      .retrieve()
      .bodyToMono(Void::class.java)
  }

  override fun get(id: String): Mono<Operation> {
    TODO("not implemented")
  }

  @Suppress("UNCHECKED_CAST")
  override fun findByBatch(batch: String): Flux<Operation> {
    return client.get().uri("/batch/$batch")
      .retrieve()
      .bodyToFlux(ImmutableOperation::class.java) as Flux<Operation>
  }

  @Suppress("UNCHECKED_CAST")
  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return client.get().uri("/target/$targetType/$targetId")
      .retrieve()
      .bodyToFlux(ImmutableOperation::class.java) as Flux<Operation>
  }
}