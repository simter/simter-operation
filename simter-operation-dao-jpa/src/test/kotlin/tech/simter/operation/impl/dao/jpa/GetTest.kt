package tech.simter.operation.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationPo
import tech.simter.operation.test.TestHelper.randomOperationId
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager

/**
 * Test [OperationDaoImpl.get].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class GetTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `get existent data without items`() {
    // init data
    val expected = randomOperationPo()
    rem.persist(expected)

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get existent data with items`() {
    // init data
    val expected = randomOperationPo().apply {
      addItem(TestHelper.randomOperationItemPo(id = "field1"))
      addItem(TestHelper.randomOperationItemPo(id = "field2"))
    }
    rem.persist(expected)

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get nonexistent data`() {
    dao.get(randomOperationId()).test().expectComplete().verify()
  }
}