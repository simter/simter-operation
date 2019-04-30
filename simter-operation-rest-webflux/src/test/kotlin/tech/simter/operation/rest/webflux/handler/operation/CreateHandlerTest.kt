package tech.simter.operation.rest.webflux.handler.operation

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import tech.simter.operation.rest.webflux.handler.TestHelper.randomOperation
import tech.simter.operation.rest.webflux.handler.UnitTestConfiguration
import tech.simter.operation.rest.webflux.handler.operation.CreateHandler.Companion.REQUEST_PREDICATE
import tech.simter.operation.service.OperationService
import java.time.format.DateTimeFormatter
import javax.json.Json

/**
 * Test [CreateHandler]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class, CreateHandler::class, CreateHandlerTest.Cfg::class)
@MockkBean(OperationService::class)
@WebFluxTest
class CreateHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val service: OperationService
) {
  @Configuration
  class Cfg {
    @Bean
    fun theRoute(handler: CreateHandler): RouterFunction<ServerResponse> = route(REQUEST_PREDICATE, handler)
  }

  @Test
  fun handle() {
    // mock
    val operation = randomOperation()
    val data = Json.createObjectBuilder()
      .add("id", operation.id)
      .add("ts", operation.ts.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
      .add("type", operation.type)
      .add("title", operation.title)
      .add("operator", Json.createObjectBuilder()
        .add("id", operation.operator.id)
        .add("name", operation.operator.name)
        .build())
      .add("target", Json.createObjectBuilder()
        .add("id", operation.target.id)
        .add("type", operation.target.type)
        .build())
    every { service.create(any()) } returns Mono.empty()

    // invoke
    val response = client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(data.build().toString())
      .exchange()

    // verify
    response.expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) {
      service.create(any())
    }
  }
}