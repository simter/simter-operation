package tech.simter.operation.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationItemPo
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationPo
import tech.simter.operation.test.TestHelper.randomOperationBatch
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
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
    val batch = randomOperationBatch()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperationPo(batch = batch, ts = now) // without items
    val operation2 = randomOperationPo(batch = batch, ts = now.minusHours(1),
      items = setOf(randomOperationItemPo(id = "field1"), randomOperationItemPo(id = "field2"))
    ) // with items
    val operation3 = randomOperationPo(batch = randomOperationBatch()) // another batch
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    dao.findByBatch(batch).test().expectNext(operation1).expectNext(operation2).verifyComplete()
  }

  @Test
  fun `found nothing 1`() {
    dao.findByBatch(randomOperationBatch()).test().verifyComplete()
  }

  @Test
  fun `found nothing 2`() {
    // init data
    rem.persist(randomOperationPo(batch = randomOperationBatch()))

    // invoke and verify
    dao.findByBatch(randomOperationBatch()).test().verifyComplete()
  }
}