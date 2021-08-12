package tech.simter.operation.rest.webflux.handler

import io.mockk.every
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import tech.simter.exception.PermissionDeniedException
import tech.simter.kotlin.data.Page
import tech.simter.operation.core.OperationService
import tech.simter.operation.core.OperationView
import tech.simter.operation.test.TestHelper.randomOperationView
import tech.simter.util.RandomUtils.randomString
import java.util.stream.Stream

/**
 * Test [FindHandler]
 *
 * @author xz
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
@TestInstance(PER_CLASS)
class FindHandlerTest @Autowired constructor(
  @Value("\${simter-operation.rest-context-path}")
  private val contextPath: String,
  private val json: Json,
  private val client: WebTestClient,
  private val service: OperationService
) {
  private fun urlProvider(): Stream<String> {
    return Stream.of(contextPath, "$contextPath/")
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `find something`(url: String) {
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
    val operation1 = randomOperationView(batch = batch1, targetType = targetType1, targetId = targetId1)
    val operation2 = randomOperationView(batch = batch2, targetType = targetType2, targetId = targetId2)
    val operationList = listOf(operation1, operation2)
    val page = Page.of(rows = operationList, total = 2, limit = pageSize, offset = 0)

    every { service.find(pageNo, pageSize, batches, targetTypes, targetIds, search) } returns Mono.just(page)

    val response = client.get().uri("$url?batch=$batch1&batch=$batch2&target-type=$targetType1" +
      "&target-type=$targetType2&target-id=$targetId1&target-id=$targetId2" +
      "&page-no=$pageNo&page-size=$pageSize&search=$search").exchange()

    val responseBody = json.encodeToString(Page.toMap(page, json))

    // invoke
    response
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(responseBody)

    // verify
    verify { service.find(pageNo, pageSize, batches, targetTypes, targetIds, search) }
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `find nothing`(url: String) {
    // mock
    val pageNo = 1
    val pageSize = 25
    val emptyList = listOf<OperationView>()
    val page = Page.of(rows = emptyList, total = 0, limit = pageSize, offset = 0)

    every { service.find(pageNo, pageSize, null, null, null, null) } returns Mono.just(page)

    val response = client.get().uri("$url?page-no=$pageNo&page-size=$pageSize").exchange()

    // invoke
    response
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.total").isEqualTo(0)
      .jsonPath("$.pageNo").isEqualTo(pageNo)
      .jsonPath("$.pageSize").isEqualTo(pageSize)
      .jsonPath("$.rows").isEmpty
    // verify
    verify { service.find(pageNo, pageSize, null, null, null, null) }
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `failed by PermissionDenied`(url: String) {
    // mock
    every { service.find() } returns Mono.error(PermissionDeniedException())

    // invoke and verify
    client.get().uri(url).exchange().expectStatus().isForbidden
    verify { service.find() }
  }
}