package tech.simter.operation.impl.dao.web

import kotlinx.serialization.encodeToString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.kotlin.data.Page
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationView
import tech.simter.reactive.context.SystemContext
import tech.simter.reactive.security.ReactiveSecurityService
import tech.simter.reactive.web.webfilter.JwtWebFilter.Companion.JWT_HEADER_NAME
import java.util.*

/**
 * The implementation of [OperationDao] by WebFlux.
 *
 * @author RJ
 */
@Repository
class OperationDaoImpl(
  private val json: kotlinx.serialization.json.Json,
  private val securityService: ReactiveSecurityService,
  @Qualifier(WEB_CLIENT_KEY)
  val webClient: WebClient
) : OperationDao {
  // get `Authorization` header value from context
  private fun getAuthorizationHeader() = SystemContext.getOptional<String>(JWT_HEADER_NAME)

  // set `Authorization` header value to request
  private fun addAuthorizationHeader(request: WebClient.RequestHeadersSpec<*>, auth: Optional<String>) =
    auth.ifPresent { request.header(JWT_HEADER_NAME, auth.get()) }

  override fun create(operation: Operation): Mono<Void> {
    return getAuthorizationHeader().flatMap {
      webClient.post().uri("/")
        .apply { addAuthorizationHeader(this, it) }
        .contentType(APPLICATION_JSON)
        .bodyValue(json.encodeToString(operation))
        //.bodyValue(toJson(operation).build().toString())
        .retrieve()
        .bodyToMono(Void::class.java)
    }
  }

  override fun create(
    type: String,
    targetType: String,
    targetId: String,
    title: String?,
    batch: String?,
    items: Set<Operation.Item>,
    remark: String?,
    result: String?
  ): Mono<Void> {
    return securityService.getAuthenticatedUser()
      .map { it.orElseGet { SystemContext.User(id = 0, account = "UNKNOWN", name = "UNKNOWN") } } // get context user info
      .flatMap { user ->
        this.create(Operation.of(
          type = type,
          operatorId = user.id.toString(),
          operatorName = user.name,
          targetType = targetType,
          targetId = targetId,
          title = title,
          batch = batch,
          items = items,
          remark = remark,
          result = result
        ))
      }
  }

  override fun get(id: String): Mono<Operation> {
    return getAuthorizationHeader().flatMap {
      webClient.get().uri("/$id")
        .apply { addAuthorizationHeader(this, it) }
        .retrieve()
        .bodyToMono(Operation.Impl::class.java)
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun findByBatch(batch: String): Flux<Operation> {
    return getAuthorizationHeader().flatMapMany {
      webClient.get().uri("/batch/$batch")
        .apply { addAuthorizationHeader(this, it) }
        .retrieve()
        .bodyToFlux(Operation.Impl::class.java) as Flux<Operation>
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun findByTarget(targetType: String, targetId: String): Flux<Operation> {
    return getAuthorizationHeader().flatMapMany {
      webClient.get().uri("/target/$targetType/$targetId")
        .apply { addAuthorizationHeader(this, it) }
        .retrieve()
        .bodyToFlux(Operation.Impl::class.java) as Flux<Operation>
    }
  }

  override fun find(
    pageNo: Int,
    pageSize: Int,
    batches: List<String>?,
    targetTypes: List<String>?,
    targetIds: List<String>?,
    search: String?
  ): Mono<Page<OperationView>> {
    TODO("not implemented")
  }
}