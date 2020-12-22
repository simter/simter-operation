package tech.simter.operation.test.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationBatch
import tech.simter.operation.test.rest.TestHelper.createOneOperation
import tech.simter.operation.test.rest.TestHelper.jsonb
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test `GET /batch/{batch}` to find all operations of specific batch.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class FindByBatchTest @Autowired constructor(
  private val client: WebTestClient
) {
  @Test
  fun `not found`() {
    client.get().uri("/batch/${randomOperationBatch()}")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `found it`() {
    // prepare data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val batch1 = randomOperationBatch()
    val batch2 = randomOperationBatch()
    val operation1 = randomOperation(batch = batch1, ts = now)
    val operation2 = randomOperation(batch = batch1, ts = now.minusSeconds(1))
    val operation3 = randomOperation(batch = batch2, ts = now.minusSeconds(2))
    createOneOperation(client = client, operation = operation1)
    createOneOperation(client = client, operation = operation2)
    createOneOperation(client = client, operation = operation3)

    // get it
    client.get().uri("/batch/$batch1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(listOf(operation1, operation2)))
  }
}