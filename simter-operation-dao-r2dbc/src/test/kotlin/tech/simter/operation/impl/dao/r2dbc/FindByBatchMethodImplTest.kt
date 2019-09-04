package tech.simter.operation.impl.dao.r2dbc

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.ImmutableOperation
import tech.simter.operation.impl.dao.r2dbc.TestHelper.randomOperation
import tech.simter.operation.impl.dao.r2dbc.TestHelper.randomOperationItem
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImplByR2dbcClient.findByBatch]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class FindByBatchMethodImplTest @Autowired constructor(
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
    // init data
    val batch = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(batch = batch, ts = now) // without items
    val operation2 = randomOperation(
      batch = batch, ts = now.minusHours(1),
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    ) // with items
    val operation3 = randomOperation(batch = randomString()) // another batch

    // invoke and verify
    saveAll(operation1, operation2, operation3)
      .thenMany(dao.findByBatch(batch))
      .test()
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
  }

  private fun saveAll(vararg list: ImmutableOperation): Mono<Void> {
    return Flux.fromArray(list).flatMap { dao.create(it).thenReturn(it) }.then()
  }

  @Test
  fun `find nothing 1`() {
    dao.findByBatch(randomString()).test().verifyComplete()
  }

  @Test
  fun `find nothing 2`() {
    dao.create(randomOperation(batch = randomString()))
      .thenMany(dao.findByBatch(randomString()))
      .test().verifyComplete()
  }
}