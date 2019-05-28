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
import reactor.core.publisher.Flux
import tech.simter.operation.rest.webflux.handler.TestHelper.randomOperation
import tech.simter.operation.rest.webflux.handler.TestHelper.randomOperationItem
import tech.simter.operation.core.OperationService
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [FindByTargetHandler]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@MockkBean(OperationService::class)
@WebFluxTest
class FindByTargetHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val mapper: ObjectMapper,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val targetType = randomString()
    val targetId = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(targetType = targetType, targetId = targetId, ts = now) // without items
    val operation2 = randomOperation(targetType = targetType, targetId = targetId, ts = now.minusHours(1)) // with items
      .apply {
        addItem(randomOperationItem(id = "field1"))
        addItem(randomOperationItem(id = "field2"))
      }
    every { service.findByTarget(targetType, targetId) } returns Flux.just(operation1, operation2)
    val responseBody = mapper.writeValueAsString(listOf(operation1, operation2))

    // invoke
    val response = client.get().uri("/target/$targetType/$targetId").exchange()

    // verify
    response.expectStatus().isOk
      .expectBody()
      .json(responseBody)
    verify(exactly = 1) { service.findByTarget(targetType, targetId) }
  }

  @Test
  fun `found nothing`() {
    // mock
    val targetType = randomString()
    val targetId = randomString()
    every { service.findByTarget(targetType, targetId) } returns Flux.empty()

    // invoke and verify
    client.get().uri("/target/$targetType/$targetId")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}