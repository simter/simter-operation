package tech.simter.operation.impl.dao.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.mongo.TestHelper.randomOperation
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl.findByBatch]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class FindByBatchMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
    // init data
    val batch = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(batch = batch, ts = now) // without items
    val operation2 = randomOperation(batch = batch, ts = now.minusHours(1),
      items = setOf(TestHelper.randomOperationItem(id = "field1"), TestHelper.randomOperationItem(id = "field2"))
    ) // with items
    val operation3 = randomOperation(batch = randomString()) // another batch
    repository
      .saveAll(listOf(operation1, operation2, operation3))
      .then().test().verifyComplete()

    // invoke and verify
    dao.findByBatch(batch).test().expectNext(operation1).expectNext(operation2).verifyComplete()
  }

  @Test
  fun `find nothing 1`() {
    dao.findByBatch(randomString()).test().verifyComplete()
  }

  @Test
  fun `find nothing 2`() {
    // init data
    repository
      .saveAll(listOf(randomOperation(batch = randomString())))
      .then().test().verifyComplete()

    // invoke and verify
    dao.findByBatch(randomString()).test().verifyComplete()
  }
}