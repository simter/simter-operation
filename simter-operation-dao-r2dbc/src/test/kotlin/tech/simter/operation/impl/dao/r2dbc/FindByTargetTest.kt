package tech.simter.operation.impl.dao.r2dbc

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl.findByTarget]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataR2dbcTest
class FindByTargetTest @Autowired constructor(
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
    // init data
    val targetType = randomString()
    val targetId = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(targetType = targetType, targetId = targetId, ts = now) // without items
    val operation2 = randomOperation(
      targetType = targetType, targetId = targetId, ts = now.plusHours(1),
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    ) // with items

    val operation3 = randomOperation(targetType = targetType, targetId = randomString()) // another targetId
    val operation4 = randomOperation(targetType = randomString(), targetId = targetId) // another targetType
    val operation5 = randomOperation(targetType = randomString(), targetId = randomString()) // another target

    // invoke and verify
    saveAll(operation1, operation2, operation3, operation4, operation5)
      .thenMany(dao.findByTarget(targetType = targetType, targetId = targetId))
      .test()
      .expectNext(operation2)
      .expectNext(operation1)
      .verifyComplete()
  }

  private fun saveAll(vararg list: Operation): Mono<Void> {
    return Flux.fromArray(list).flatMap { dao.create(it).thenReturn(it) }.then()
  }

  @Test
  fun `find nothing 1`() {
    dao.findByTarget(targetType = randomString(), targetId = randomString())
      .test().verifyComplete()
  }

  @Test
  fun `find nothing 2`() {
    dao.create(randomOperation(batch = randomString()))
      .thenMany(dao.findByTarget(targetType = randomString(), targetId = randomString()))
      .test().verifyComplete()
  }
}