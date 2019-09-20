package tech.simter.operation.rest.webflux.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import tech.simter.exception.PermissionDeniedException
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService
import tech.simter.operation.rest.webflux.convert
import tech.simter.operation.rest.webflux.handler.TestHelper.randomOperation
import tech.simter.util.RandomUtils.randomString

/**
 * Test [FindHandler]
 *
 * @author xz
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@MockkBean(OperationService::class)
@WebFluxTest
class FindHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val mapper: ObjectMapper,
  private val service: OperationService
) {
  @Test
  fun `find something`() {
    // mock
    val targetType1 = randomString()
    val targetType2 = randomString()
    val targetTypes = listOf(targetType1,targetType2)
    val pageNo = 1
    val pageSize = 25
    val search = randomString()
    val operation1 = randomOperation(targetType = targetType1)
    val operation2 = randomOperation(targetType = targetType2)
    val operationList = listOf(operation1,operation2)
    val page = PageImpl(operationList, PageRequest.of(pageNo - 1, pageSize), 2)

    every { service.find(targetTypes, pageNo, pageSize, search) } returns Mono.just(page)

    val response = client.get().uri("/?target-type=${targetTypes.joinToString(",")}&page-no=$pageNo&page-size=$pageSize&search=$search").exchange()
    val responseBody = mapper.writeValueAsString(page.convert())

    // invoke
    response
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(responseBody)

    // verify
    verify { service.find(targetTypes, pageNo, pageSize, search) }
  }

  @Test
  fun `find nothing`() {
    // mock
    val pageNo = 1
    val pageSize = 25
    val emptyList = listOf<Operation>()
    val page = PageImpl(emptyList, PageRequest.of(pageNo - 1, pageSize), 0)

    every { service.find(null, pageNo, pageSize,null) } returns Mono.just(page)

    val response = client.get().uri("/?page-no=$pageNo&page-size=$pageSize").exchange()

    // invoke
    response
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.count").isEqualTo(0)
      .jsonPath("$.pageNo").isEqualTo(pageNo)
      .jsonPath("$.pageSize").isEqualTo(pageSize)
      .jsonPath("$.rows").isEmpty
    // verify
    verify { service.find(null, pageNo, pageSize, null) }
  }

  @Test
  fun `failed by PermissionDenied`() {
    // mock
    every { service.find() } returns Mono.error(PermissionDeniedException())

    // invoke and verify
    client.get().uri("/").exchange().expectStatus().isForbidden
    verify { service.find() }
  }
}