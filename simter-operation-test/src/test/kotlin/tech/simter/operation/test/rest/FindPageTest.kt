package tech.simter.operation.test.rest

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
import tech.simter.kotlin.data.Page
import tech.simter.operation.core.OperationView
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationBatch
import tech.simter.operation.test.TestHelper.randomOperationTargetId
import tech.simter.operation.test.TestHelper.randomOperationTargetType
import tech.simter.operation.test.TestHelper.randomOperationTitle
import tech.simter.operation.test.rest.UnitTestConfiguration
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.stream.Stream

/**
 * Test `GET /operation` to find pageable operations.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
@TestInstance(PER_CLASS)
class FindPageTest @Autowired constructor(
  @Value("\${server.context-path}")
  private val contextPath: String,
  private val json: Json,
  private val client: WebTestClient,
  private val helper: TestHelper
) {
  private fun urlProvider(): Stream<String> {
    return Stream.of(contextPath, "$contextPath/")
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `not found`(url: String) {
    client.get().uri("$url?batch=${randomOperationBatch()}")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json("""{"count":0,"pageNo":1,"pageSize":25,"rows":[]}""")
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `find page by target`(url: String) {
    // prepare data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val targetType1 = randomOperationTargetType()
    val targetType2 = randomOperationTargetType()
    val targetId1 = randomOperationTargetId()
    val targetId2 = randomOperationTargetId()
    val operation1 = randomOperation(targetType = targetType1, targetId = targetId1, ts = now)
    val operation2 = randomOperation(targetType = targetType1, targetId = targetId2, ts = now.minusSeconds(1))
    val operation3 = randomOperation(targetType = targetType2, targetId = targetId1, ts = now.minusSeconds(2))
    helper.createOne(operation = operation1)
    helper.createOne(operation = operation2)
    helper.createOne(operation = operation3)

    // 1. find all targets
    var rows = listOf(OperationView.from(operation1), OperationView.from(operation2), OperationView.from(operation3))
    client.get().uri("$url?target-type=$targetType1&target-type=$targetType2")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(json.encodeToString(Page.toMap(
        page = Page.of(
          limit = 25,
          offset = 0L,
          total = rows.size.toLong(),
          rows = rows
        ),
        json = json
      )))

    // 2. find only targetType1
    rows = listOf(OperationView.from(operation1), OperationView.from(operation2))
    client.get().uri("$url?target-type=$targetType1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(json.encodeToString(Page.toMap(
        page = Page.of(
          limit = 25,
          offset = 0L,
          total = rows.size.toLong(),
          rows = rows
        ),
        json = json
      )))

    // 3. find only targetId1
    rows = listOf(OperationView.from(operation1), OperationView.from(operation3))
    client.get().uri("$url?target-id=$targetId1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(json.encodeToString(Page.toMap(
        page = Page.of(
          limit = 25,
          offset = 0L,
          total = rows.size.toLong(),
          rows = rows
        ),
        json = json
      )))
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `find page by batch`(url: String) {
    // prepare data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val batch1 = randomOperationBatch()
    val batch2 = randomOperationBatch()
    val operation1 = randomOperation(batch = batch1, ts = now)
    val operation2 = randomOperation(batch = batch1, ts = now.minusSeconds(1))
    val operation3 = randomOperation(batch = batch2, ts = now.minusSeconds(2))
    helper.createOne(operation = operation1)
    helper.createOne(operation = operation2)
    helper.createOne(operation = operation3)

    // 1. find all batches
    var rows = listOf(OperationView.from(operation1), OperationView.from(operation2), OperationView.from(operation3))
    client.get().uri("$url?batch=$batch1&batch=$batch2")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(json.encodeToString(Page.toMap(
        page = Page.of(
          limit = 25,
          offset = 0L,
          total = rows.size.toLong(),
          rows = rows
        ),
        json = json
      )))

    // 2. find only batch1
    rows = listOf(OperationView.from(operation1), OperationView.from(operation2))
    client.get().uri("$url?batch=$batch1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(json.encodeToString(Page.toMap(
        page = Page.of(
          limit = 25,
          offset = 0L,
          total = rows.size.toLong(),
          rows = rows
        ),
        json = json
      )))
  }

  @ParameterizedTest
  @MethodSource("urlProvider")
  fun `find page by search title`(url: String) {
    // prepare data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val title1 = randomOperationTitle(prefix = "title1")
    val title2 = randomOperationTitle(prefix = "title2")
    val operation1 = randomOperation(title = title1, ts = now)
    val operation2 = randomOperation(title = title2, ts = now.minusSeconds(1))
    helper.createOne(operation = operation1)
    helper.createOne(operation = operation2)

    // search title1
    val rows = listOf(OperationView.from(operation1))
    client.get().uri("$url?search=$title1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(json.encodeToString(Page.toMap(
        page = Page.of(
          limit = 25,
          offset = 0L,
          total = rows.size.toLong(),
          rows = rows
        ),
        json = json
      )))
  }
}