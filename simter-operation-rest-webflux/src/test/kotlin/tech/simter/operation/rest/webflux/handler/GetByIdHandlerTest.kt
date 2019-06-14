package tech.simter.operation.rest.webflux.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import tech.simter.operation.core.OperationService
import tech.simter.util.RandomUtils
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [GetByIdHandler]
 *
 * @author zf
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@MockkBean(OperationService::class)
@WebFluxTest
class GetByIdHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val mapper: ObjectMapper,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val id = RandomUtils.randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation = TestHelper.randomOperation(
      ts = now.minusHours(1),
      items = setOf(TestHelper.randomOperationItem(id = "field1"), TestHelper.randomOperationItem(id = "field2"))
    ) // with items
    every { service.get(id) } returns Mono.just(operation)
    val responseBody = mapper.writeValueAsString(operation)

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
    val id = RandomUtils.randomString()
    every { service.get(id) } returns Mono.empty()

    // invoke and verify
    client.get().uri("/$id")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}
