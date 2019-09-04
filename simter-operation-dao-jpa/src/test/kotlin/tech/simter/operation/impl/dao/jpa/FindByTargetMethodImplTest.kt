package tech.simter.operation.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperation
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationItem
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl.findByTarget]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class FindByTargetMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `found something`() {
    // init data
    val targetType = randomString()
    val targetId = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(targetType = targetType, targetId = targetId, ts = now) // without items
    val operation2 = randomOperation(targetType = targetType, targetId = targetId, ts = now.plusHours(1),
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    ) // with items
    val operation3 = randomOperation(targetType = targetType, targetId = randomString()) // another targetId
    val operation4 = randomOperation(targetType = randomString(), targetId = targetId) // another targetType
    val operation5 = randomOperation(targetType = randomString(), targetId = randomString()) // another target
    rem.persist(operation1, operation2, operation3, operation4, operation5)

    // invoke and verify
    dao.findByTarget(targetType, targetId).test().expectNext(operation2).expectNext(operation1).verifyComplete()
  }

  @Test
  fun `found nothing 1`() {
    val targetType = randomString()
    val targetId = randomString()
    dao.findByTarget(targetType, targetId).test().verifyComplete()
  }

  @Test
  fun `found nothing 2`() {
    // init data
    rem.persist(randomOperation())

    // invoke and verify
    val targetType = randomString()
    val targetId = randomString()
    dao.findByTarget(targetType, targetId).test().verifyComplete()
  }
}