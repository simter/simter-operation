package tech.simter.operation.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationItemPo
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationPo
import tech.simter.operation.test.TestHelper.randomOperationTargetId
import tech.simter.operation.test.TestHelper.randomOperationTargetType
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
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
class FindByTargetTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `found something`() {
    // init data
    val targetType = randomOperationTargetType()
    val targetId = randomOperationTargetId()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperationPo(targetType = targetType, targetId = targetId, ts = now) // without items
    val operation2 = randomOperationPo(targetType = targetType, targetId = targetId, ts = now.plusHours(1),
      items = setOf(randomOperationItemPo(id = "field1"), randomOperationItemPo(id = "field2"))
    ) // with items
    val operation3 = randomOperationPo(targetType = targetType, targetId = randomOperationTargetId()) // another targetId
    val operation4 = randomOperationPo(targetType = randomOperationTargetType(), targetId = targetId) // another targetType
    val operation5 = randomOperationPo(targetType = randomOperationTargetType(), targetId = randomOperationTargetId()) // another target
    rem.persist(operation1, operation2, operation3, operation4, operation5)

    // invoke and verify
    dao.findByTarget(targetType, targetId).test().expectNext(operation2).expectNext(operation1).verifyComplete()
  }

  @Test
  fun `found nothing 1`() {
    val targetType = randomOperationTargetType()
    val targetId = randomOperationTargetId()
    dao.findByTarget(targetType, targetId).test().verifyComplete()
  }

  @Test
  fun `found nothing 2`() {
    // init data
    rem.persist(randomOperationPo())

    // invoke and verify
    val targetType = randomOperationTargetType()
    val targetId = randomOperationTargetId()
    dao.findByTarget(targetType, targetId).test().verifyComplete()
  }
}