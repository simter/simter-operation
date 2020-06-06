package tech.simter.operation.test.rest

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.operation.core.Operation
import tech.simter.operation.test.TestHelper.randomOperation
import javax.json.bind.Jsonb
import javax.json.bind.JsonbBuilder

object TestHelper {
  /** a share jsonb instance */
  val jsonb: Jsonb = JsonbBuilder.create()

  /** create one operation */
  fun createOneOperation(
    client: WebTestClient,
    operation: Operation = randomOperation()
  ): Operation {
    client.post()
      .uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(jsonb.toJson(operation))
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty

    return operation
  }
}