package tech.simter.operation.test.rest

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.operation.core.Operation
import tech.simter.operation.test.TestHelper.randomOperation

@Component
class TestHelper @Autowired constructor(
  @Value("\${server.context-path}")
  private val contextPath: String,
  private val json: Json,
  private val client: WebTestClient
) {
  /** create one operation */
  fun createOne(
    operation: Operation = randomOperation()
  ): Operation {
    client.post()
      .uri(contextPath)
      .contentType(APPLICATION_JSON)
      .bodyValue(json.encodeToString(operation))
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty

    return operation
  }
}