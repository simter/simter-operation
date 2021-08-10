package tech.simter.operation.test.rest

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
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem
import tech.simter.operation.test.rest.UnitTestConfiguration
import java.util.stream.Stream

/**
 * Test `POST /operation` to create an operation.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
@TestInstance(PER_CLASS)
class CreateTest @Autowired constructor(
  @Value("\${server.context-path}")
  private val contextPath: String,
  private val json: Json,
  private val client: WebTestClient
) {
  private fun urlProvider(): Stream<String> {
    return Stream.of(contextPath, "$contextPath/")
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `create one`(url: String) {
    // mock
    val kv = randomOperation(items = setOf(randomOperationItem()))
    val data = json.encodeToString(kv)

    // invoke and verify
    client.post().uri(url)
      .contentType(APPLICATION_JSON)
      .bodyValue(data)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `ignore by empty json`(url: String) {
    // invoke and verify
    client.post().uri(url)
      .contentType(APPLICATION_JSON)
      .bodyValue("{}")
      .exchange()
      .expectStatus().isBadRequest
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `ignore by empty body`(url: String) {
    // invoke and verify
    client.post().uri(url)
      .contentType(APPLICATION_JSON)
      .bodyValue("")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `ignore by without body`(url: String) {
    // invoke and verify
    client.post().uri(url)
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}