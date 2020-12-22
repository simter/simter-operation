package tech.simter.operation.test.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.operation.core.OperationView
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationBatch
import tech.simter.operation.test.TestHelper.randomOperationTargetId
import tech.simter.operation.test.TestHelper.randomOperationTargetType
import tech.simter.operation.test.TestHelper.randomOperationTitle
import tech.simter.operation.test.rest.TestHelper.createOneOperation
import tech.simter.operation.test.rest.TestHelper.jsonb
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test `GET /` to find pageable operations.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class FindPageTest @Autowired constructor(
  private val client: WebTestClient
) {
  @Test
  fun `not found`() {
    client.get().uri("/?batch=${randomOperationBatch()}")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json("""{"count":0,"pageNo":1,"pageSize":25,"rows":[]}""")
  }

  @Test
  fun `find page by target`() {
    // prepare data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val targetType1 = randomOperationTargetType()
    val targetType2 = randomOperationTargetType()
    val targetId1 = randomOperationTargetId()
    val targetId2 = randomOperationTargetId()
    val operation1 = randomOperation(targetType = targetType1, targetId = targetId1, ts = now)
    val operation2 = randomOperation(targetType = targetType1, targetId = targetId2, ts = now.minusSeconds(1))
    val operation3 = randomOperation(targetType = targetType2, targetId = targetId1, ts = now.minusSeconds(2))
    createOneOperation(client = client, operation = operation1)
    createOneOperation(client = client, operation = operation2)
    createOneOperation(client = client, operation = operation3)

    // 1. find all targets
    var rows = listOf(OperationView.from(operation1), OperationView.from(operation2), OperationView.from(operation3))
    client.get().uri("/?target-type=$targetType1&target-type=$targetType2")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(mapOf(
        "count" to rows.size,
        "pageNo" to 1,
        "pageSize" to 25,
        "rows" to rows))
      )

    // 2. find only targetType1
    rows = listOf(OperationView.from(operation1), OperationView.from(operation2))
    client.get().uri("/?target-type=$targetType1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(mapOf(
        "count" to rows.size,
        "pageNo" to 1,
        "pageSize" to 25,
        "rows" to rows))
      )

    // 3. find only targetId1
    rows = listOf(OperationView.from(operation1), OperationView.from(operation3))
    client.get().uri("/?target-id=$targetId1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(mapOf(
        "count" to rows.size,
        "pageNo" to 1,
        "pageSize" to 25,
        "rows" to rows))
      )
  }

  @Test
  fun `find page by batch`() {
    // prepare data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val batch1 = randomOperationBatch()
    val batch2 = randomOperationBatch()
    val operation1 = randomOperation(batch = batch1, ts = now)
    val operation2 = randomOperation(batch = batch1, ts = now.minusSeconds(1))
    val operation3 = randomOperation(batch = batch2, ts = now.minusSeconds(2))
    createOneOperation(client = client, operation = operation1)
    createOneOperation(client = client, operation = operation2)
    createOneOperation(client = client, operation = operation3)

    // 1. find all batches
    var rows = listOf(OperationView.from(operation1), OperationView.from(operation2), OperationView.from(operation3))
    client.get().uri("/?batch=$batch1&batch=$batch2")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(mapOf(
        "count" to rows.size,
        "pageNo" to 1,
        "pageSize" to 25,
        "rows" to rows))
      )

    // 2. find only batch1
    rows = listOf(OperationView.from(operation1), OperationView.from(operation2))
    client.get().uri("/?batch=$batch1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(mapOf(
        "count" to rows.size,
        "pageNo" to 1,
        "pageSize" to 25,
        "rows" to rows))
      )
  }

  @Test
  fun `find page by search title`() {
    // prepare data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val title1 = randomOperationTitle(prefix = "title1")
    val title2 = randomOperationTitle(prefix = "title2")
    val operation1 = randomOperation(title = title1, ts = now)
    val operation2 = randomOperation(title = title2, ts = now.minusSeconds(1))
    createOneOperation(client = client, operation = operation1)
    createOneOperation(client = client, operation = operation2)

    // search title1
    val rows = listOf(OperationView.from(operation1))
    client.get().uri("/?search=$title1")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .json(jsonb.toJson(mapOf(
        "count" to rows.size,
        "pageNo" to 1,
        "pageSize" to 25,
        "rows" to rows))
      )
  }
}