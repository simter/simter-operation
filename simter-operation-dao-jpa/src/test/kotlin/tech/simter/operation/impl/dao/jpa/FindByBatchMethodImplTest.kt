package tech.simter.operation.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperation
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
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
@ReactiveDataJpaTest
class FindByBatchMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `found something`() {
    // init data
    val batch = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(batch = batch, ts = now) // without items
    val operation2 = randomOperation(batch = batch, ts = now.minusHours(1)) // with items
      .apply {
        addItem(TestHelper.randomOperationItem(id = "field1"))
        addItem(TestHelper.randomOperationItem(id = "field2"))
      }
    val operation3 = randomOperation(batch = randomString()) // another batch
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    dao.findByBatch(batch).test().expectNext(operation1).expectNext(operation2).verifyComplete()
  }

  @Test
  fun `found nothing 1`() {
    dao.findByBatch(randomString()).test().verifyComplete()
  }

  @Test
  fun `found nothing 2`() {
    // init data
    rem.persist(randomOperation(batch = randomString()))

    // invoke and verify
    dao.findByBatch(randomString()).test().verifyComplete()
  }
}