package tech.simter.operation.starter

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * Run test in the real server.
 *
 * @author RJ
 */
@Disabled
class IntegrationTest {
  private val client = WebTestClient.bindToServer().baseUrl("http://localhost:9014").build()
  private val contextPath = ""

  @Test
  fun `Show root page`() {
    // TODO
  }
}