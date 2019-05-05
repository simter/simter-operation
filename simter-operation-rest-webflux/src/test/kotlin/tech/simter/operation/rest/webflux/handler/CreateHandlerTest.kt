package tech.simter.operation.rest.webflux.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import tech.simter.operation.rest.webflux.handler.TestHelper.randomOperation
import tech.simter.operation.rest.webflux.handler.TestHelper.randomOperationItem
import tech.simter.operation.service.OperationService
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [CreateHandler]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@MockkBean(OperationService::class)
@WebFluxTest
class CreateHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val mapper: ObjectMapper,
  private val service: OperationService
) {
  @Test
  fun `success without items`() {
    // mock
    val operation = randomOperation(ts = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES))
    every { service.create(operation) } returns Mono.empty()

    // invoke
    val requestBody = mapper.writeValueAsString(operation)
    val response = client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(requestBody)
      .exchange()

    // verify
    response.expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.create(operation) }
  }

  @Test
  fun `success with items`() {
    // mock
    val operation = randomOperation(ts = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES)).apply {
      addItem(randomOperationItem(id = "field1"))
      addItem(randomOperationItem(id = "field2"))
    }
    every { service.create(operation) } returns Mono.empty()

    // invoke
    val requestBody = mapper.writeValueAsString(operation)
    //println("requestBody=$requestBody")
    val response = client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(requestBody)
      .exchange()

    // verify
    response.expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.create(operation) }
  }
}