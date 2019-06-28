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
import tech.simter.reactive.context.SystemContext
import tech.simter.reactive.web.Utils.createWebClient
import tech.simter.reactive.web.webfilter.JwtWebFilter.Companion.JWT_HEADER_NAME
import java.time.format.DateTimeFormatter
import java.util.*
import javax.json.Json
import javax.json.JsonObjectBuilder

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
  private val client: WebClient = createWebClient(
    baseUrl = serverAddress,
    proxyHost = proxyHost,
    proxyPort = proxyPort
  )

  // get `Authorization` header value from context
  private fun getAuthorizationHeader() = SystemContext.getOptional<String>(JWT_HEADER_NAME)

  // set `Authorization` header value to request
  private fun addAuthorizationHeader(request: WebClient.RequestHeadersSpec<*>, auth: Optional<String>) =
    auth.ifPresent { request.header(JWT_HEADER_NAME, auth.get()) }

  override fun create(operation: Operation): Mono<Void> {
    return getAuthorizationHeader().flatMap {
      client.post()
        .apply { addAuthorizationHeader(this, it) }
        .contentType(APPLICATION_JSON)
        .syncBody(toJson(operation).build().toString())
        .retrieve()
        .bodyToMono(Void::class.java)
    }
  }

  override fun get(id: String): Mono<Operation> {
    return getAuthorizationHeader().flatMap {
      client.get().uri("/$id")
        .apply { addAuthorizationHeader(this, it) }
        .retrieve()
        .bodyToMono(ImmutableOperation::class.java)
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun findByBatch(batch: String): Flux<Operation> {
    return getAuthorizationHeader().flatMapMany {
      client.get().uri("/batch/$batch")
        .apply { addAuthorizationHeader(this, it) }
        .retrieve()
        .bodyToFlux(ImmutableOperation::class.java) as Flux<Operation>
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return getAuthorizationHeader().flatMapMany {
      client.get().uri("/target/$targetType/$targetId")
        .apply { addAuthorizationHeader(this, it) }
        .retrieve()
        .bodyToFlux(ImmutableOperation::class.java) as Flux<Operation>
    }
  }

  private val tsFormatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
  private fun toJson(operation: Operation): JsonObjectBuilder {
    val builder = Json.createObjectBuilder()
      .add("id", operation.id)
      .add("ts", operation.ts.format(tsFormatter))
      .add("type", operation.type)
      .add("operatorId", operation.operatorId)
      .add("operatorName", operation.operatorName)
      .add("targetId", operation.targetId)
      .add("targetType", operation.targetType)
    if (operation.title != null) builder.add("title", operation.title)
    if (operation.remark != null) builder.add("remark", operation.remark)
    if (operation.result != null) builder.add("result", operation.result)
    if (operation.batch != null) builder.add("batch", operation.batch)
    if (operation.items.isNotEmpty()) {
      val items = Json.createArrayBuilder()
      operation.items.forEach { items.add(toJson(it)) }
      builder.add("items", items)
    }
    return builder
  }

  private fun toJson(item: Operation.Item): JsonObjectBuilder {
    val builder = Json.createObjectBuilder()
      .add("id", item.id)
      .add("valueType", item.valueType)
    if (item.title != null) builder.add("title", item.title)
    if (item.oldValue != null) builder.add("oldValue", item.oldValue)
    if (item.newValue != null) builder.add("newValue", item.newValue)
    return builder
  }
}