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
import tech.simter.operation.test.TestHelper.randomOperation
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
    val pageNo = 1
    val pageSize = 25
    val batch1 = randomString()
    val batch2 = randomString()
    val batches = listOf(batch1, batch2)
    val targetType1 = randomString()
    val targetType2 = randomString()
    val targetTypes = listOf(targetType1, targetType2)
    val targetId1 = randomString()
    val targetId2 = randomString()
    val targetIds = listOf(targetId1, targetId2)
    val search = randomString()
    val operation1 = randomOperation(batch = batch1, targetType = targetType1, targetId = targetId1)
    val operation2 = randomOperation(batch = batch2, targetType = targetType2, targetId = targetId2)
    val operationList = listOf(operation1, operation2)
    val page = PageImpl(operationList, PageRequest.of(pageNo - 1, pageSize), 2)

    every { service.find(pageNo, pageSize, batches, targetTypes, targetIds, search) } returns Mono.just(page)

    val response = client.get().uri("/?batch=$batch1&batch=$batch2&target-type=$targetType1" +
      "&target-type=$targetType2&target-id=$targetId1&target-id=$targetId2" +
      "&page-no=$pageNo&page-size=$pageSize&search=$search").exchange()

    val responseBody = mapper.writeValueAsString(page.convert())

    // invoke
    response
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(responseBody)

    // verify
    verify { service.find(pageNo, pageSize, batches, targetTypes, targetIds, search) }
  }

  @Test
  fun `find nothing`() {
    // mock
    val pageNo = 1
    val pageSize = 25
    val emptyList = listOf<Operation>()
    val page = PageImpl(emptyList, PageRequest.of(pageNo - 1, pageSize), 0)

    every { service.find(pageNo, pageSize, null, null, null, null) } returns Mono.just(page)

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
    verify { service.find(pageNo, pageSize, null, null, null, null) }
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