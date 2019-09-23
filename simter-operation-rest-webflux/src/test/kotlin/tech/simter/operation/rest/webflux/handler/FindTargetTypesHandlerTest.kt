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
import tech.simter.operation.core.OperationService
import tech.simter.util.RandomUtils.randomString

/**
 * Test [FindByTargetHandler]
 *
 * @author xz
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@MockkBean(OperationService::class)
@WebFluxTest
class FindTargetTypesHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val mapper: ObjectMapper,
  private val service: OperationService
) {
  @Test
  fun `find something`() {
    // mock
    val targetType1 = randomString()
    val targetType2 = randomString()
    var list = listOf(targetType1, targetType2)

    every { service.findTargetTypes() } returns Flux.just(targetType1,targetType2)
    val responseBody = mapper.writeValueAsString(list)

    // invoke
    val response = client.get().uri("/target-type").exchange()

    // verify
    response.expectStatus().isOk
      .expectBody()
      .json(responseBody)
    verify(exactly = 1) { service.findTargetTypes() }
  }

  @Test
  fun `find nothing`() {
    every { service.findTargetTypes() } returns Flux.empty()

    // invoke and verify
    client.get().uri("/target-type")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}