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

/**
 * Test [OperationDaoImpl.findByTarget]
 *
 * @author xz
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class FindTargetTypesMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `found something`() {
    // mock
    val targetType1 = randomString()
    val targetType2 = randomString()
    val operation1 = randomOperation(targetType = targetType1)
    val operation2 = randomOperation(targetType = targetType2)
    val operation3 = randomOperation(targetType = targetType2)
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    dao.findTargetTypes().test().expectNextCount(2).verifyComplete()
  }

  @Test
  fun `found nothing`() {
    // invoke and verify
    dao.findTargetTypes().test().verifyComplete()
  }
}