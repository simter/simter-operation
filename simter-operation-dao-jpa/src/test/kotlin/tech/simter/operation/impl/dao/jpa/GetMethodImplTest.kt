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
 * Test [OperationDaoImpl.get].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class GetMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `get existent data without items`() {
    // init data
    val expected = randomOperation()
    rem.persist(expected)

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get existent data with items`() {
    // init data
    val expected = randomOperation().apply {
      addItem(TestHelper.randomOperationItem(id = "field1"))
      addItem(TestHelper.randomOperationItem(id = "field2"))
    }
    rem.persist(expected)

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get nonexistent data`() {
    dao.get(randomString()).test().expectComplete().verify()
  }
}