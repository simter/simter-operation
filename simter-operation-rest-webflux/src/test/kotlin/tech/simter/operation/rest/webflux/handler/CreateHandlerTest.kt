package tech.simter.operation.rest.webflux.handler

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import tech.simter.operation.core.OperationService
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit.MINUTES
import java.util.stream.Stream

/**
 * Test [CreateHandler]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@MockkBean(OperationService::class)
@WebFluxTest
@TestInstance(PER_CLASS)
class CreateHandlerTest @Autowired constructor(
  @Value("\${simter-operation.rest-context-path}")
  private val contextPath: String,
  private val json: Json,
  private val client: WebTestClient,
  private val service: OperationService
) {
  private fun urlProvider(): Stream<String> {
    return Stream.of(contextPath, "$contextPath/")
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `success without items`(url: String) {
    // mock
    val operation = randomOperation(ts = OffsetDateTime.now().truncatedTo(MINUTES))
    every { service.create(operation) } returns Mono.empty()

    // invoke
    val requestBody = json.encodeToString(operation)
    val response = client.post().uri(url)
      .contentType(APPLICATION_JSON)
      .bodyValue(requestBody)
      .exchange()

    // verify
    response.expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.create(operation) }
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `success with items`(url: String) {
    // mock
    val operation = randomOperation(
      ts = OffsetDateTime.now().truncatedTo(MINUTES),
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    )
    every { service.create(operation) } returns Mono.empty()

    // invoke
    val requestBody = json.encodeToString(operation)
    val response = client.post().uri(url)
      .contentType(APPLICATION_JSON)
      .bodyValue(requestBody)
      .exchange()

    // verify
    response.expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.create(operation) }
  }
}