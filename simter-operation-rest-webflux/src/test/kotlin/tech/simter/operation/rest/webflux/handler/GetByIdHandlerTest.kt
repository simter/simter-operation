package tech.simter.operation.rest.webflux.handler

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono
import tech.simter.exception.PermissionDeniedException
import tech.simter.operation.core.OperationService
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit.SECONDS

/**
 * Test [GetByIdHandler]
 *
 * @author zf
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@MockkBean(OperationService::class)
@WebFluxTest
class GetByIdHandlerTest @Autowired constructor(
  private val json: Json,
  private val client: WebTestClient,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val id = randomString()
    val now = OffsetDateTime.now().truncatedTo(SECONDS)
    val operation = randomOperation(
      ts = now.minusHours(1),
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    ) // with items
    every { service.get(id) } returns Mono.just(operation)
    val responseBody = json.encodeToString(operation)

    // invoke
    val response = client.get().uri("/$id").exchange()

    // verify
    response.expectStatus().isOk
      .expectBody()
      .json(responseBody)
    verify(exactly = 1) { service.get(id) }
  }

  @Test
  fun `found nothing`() {
    // mock
    val id = randomString()
    every { service.get(id) } returns Mono.empty()

    // invoke and verify
    client.get().uri("/$id")
      .exchange()
      .expectStatus().isNotFound
      .expectBody().isEmpty
  }

  @Test
  fun `failed by permission denied`() {
    // mock
    val id = randomString()
    val msg = randomString()
    every { service.get(id) } returns Mono.error(PermissionDeniedException(msg))

    // invoke and verify
    client.get().uri("/$id")
      .exchange()
      .expectStatus().isForbidden
      .expectHeader().contentTypeCompatibleWith(TEXT_PLAIN)
      .expectBody<String>().isEqualTo(msg)
    verify { service.get(id) }
  }
}