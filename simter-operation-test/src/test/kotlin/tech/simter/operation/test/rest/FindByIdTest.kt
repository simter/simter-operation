package tech.simter.operation.test.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationId
import tech.simter.operation.test.rest.TestHelper.createOneOperation
import tech.simter.operation.test.rest.TestHelper.jsonb

/**
 * Test find by batch.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class FindByIdTest @Autowired constructor(
  private val client: WebTestClient
) {
  @Test
  fun `not found`() {
    client.get().uri("/${randomOperationId()}")
      .exchange()
      .expectStatus().isNotFound
      .expectBody().isEmpty
  }

  @Test
  fun `get it`() {
    // prepare data
    val operation = randomOperation()
    createOneOperation(client = client, operation = operation)

    // get it
    client.get().uri("/${operation.id}")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(operation))
  }
}