package tech.simter.operation.impl.dao.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.mongo.TestHelper.randomOperationItemPo
import tech.simter.operation.impl.dao.mongo.TestHelper.randomOperationPo
import tech.simter.operation.test.TestHelper.randomOperationBatch
import tech.simter.operation.test.TestHelper.randomOperationTargetId
import tech.simter.operation.test.TestHelper.randomOperationTargetType
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl.findByTarget]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class FindByTargetMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
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
    repository
      .saveAll(listOf(operation1, operation2, operation3, operation4, operation5))
      .then().test().verifyComplete()

    // invoke and verify
    dao.findByTarget(targetType, targetId).test().expectNext(operation2).expectNext(operation1).verifyComplete()
  }

  @Test
  fun `find nothing 1`() {
    val targetType = randomOperationTargetType()
    val targetId = randomOperationTargetId()
    dao.findByTarget(targetType, targetId).test().verifyComplete()
  }

  @Test
  fun `find nothing 2`() {
    // init data
    repository
      .saveAll(listOf(randomOperationPo(batch = randomOperationBatch()), randomOperationPo()))
      .then().test().verifyComplete()

    // invoke and verify
    val targetType = randomOperationTargetType()
    val targetId = randomOperationTargetId()
    dao.findByTarget(targetType, targetId).test().verifyComplete()
  }
}