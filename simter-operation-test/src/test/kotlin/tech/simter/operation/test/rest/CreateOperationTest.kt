package tech.simter.operation.test.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.rest.TestHelper.jsonb

/**
 * Test create.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class CreateOperationTest @Autowired constructor(
  private val client: WebTestClient
) {
  @Test
  fun `create one`() {
    // mock
    val kv = randomOperation()
    val data = jsonb.toJson(kv)
    //println("----------json=$data")

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(data)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `ignore by empty json`() {
    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue("{}")
      .exchange()
      .expectStatus().isBadRequest
  }

  @Test
  fun `ignore by empty body`() {
    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue("")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `ignore by without body`() {
    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}