package tech.simter.operation.test.rest

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationId
import tech.simter.operation.test.rest.UnitTestConfiguration

/**
 * Test `GET /operation/$id` to load an operation.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class GetByIdTest @Autowired constructor(
  @Value("\${server.context-path}")
  private val contextPath: String,
  private val json: Json,
  private val client: WebTestClient,
  private val helper: TestHelper
) {
  @Test
  fun `not found`() {
    client.get().uri("$contextPath/${randomOperationId()}")
      .exchange()
      .expectStatus().isNotFound
      .expectBody().isEmpty
  }

  @Test
  fun `get it`() {
    // prepare data
    val operation = helper.createOne(randomOperation())

    // get it
    client.get().uri("$contextPath/${operation.id}")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(json.encodeToString(operation))
  }
}