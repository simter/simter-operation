package tech.simter.operation.rest.webflux.handler

import io.mockk.every
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import tech.simter.exception.PermissionDeniedException
import tech.simter.operation.core.OperationService
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit.SECONDS

/**
 * Test [FindByBatchHandler]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class FindByBatchHandlerTest @Autowired constructor(
  @Value("\${simter-operation.rest-context-path}")
  private val contextPath: String,
  private val json: Json,
  private val client: WebTestClient,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val batch = randomString()
    val now = OffsetDateTime.now().truncatedTo(SECONDS)
    val operation1 = randomOperation(batch = batch, ts = now) // without items
    val operation2 = randomOperation(
      batch = batch, ts = now.minusHours(1),
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    ) // with items
    every { service.findByBatch(batch) } returns Flux.just(operation1, operation2)
    val responseBody = json.encodeToString(listOf(operation1, operation2))

    // invoke
    val response = client.get().uri("$contextPath/batch/$batch").exchange()

    // verify
    response.expectStatus().isOk
      .expectBody()
      .json(responseBody)
    verify(exactly = 1) { service.findByBatch(batch) }
  }

  @Test
  fun `found nothing`() {
    // mock
    val batch = randomString()
    every { service.findByBatch(batch) } returns Flux.empty()

    // invoke and verify
    client.get().uri("$contextPath/batch/$batch")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `failed by PermissionDenied`() {
    // mock
    val batch = randomString()
    every { service.findByBatch(batch) } returns Flux.error(PermissionDeniedException())

    // invoke and verify
    client.get().uri("$contextPath/batch/$batch").exchange().expectStatus().isForbidden
    verify { service.findByBatch(batch) }
  }
}