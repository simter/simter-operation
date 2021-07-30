package tech.simter.operation.impl.dao.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.mongo.TestHelper.randomOperationItemPo
import tech.simter.operation.impl.dao.mongo.TestHelper.randomOperationPo
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationDaoImpl.get].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class GetTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `get existent data without items`() {
    // init data
    val expected = repository.save(randomOperationPo()).block()!!

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get existent data with items`() {
    // init data
    val expected = repository.save(randomOperationPo(
      items = setOf(randomOperationItemPo(id = "field1"), randomOperationItemPo(id = "field2"))
    )).block()!!

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }


  @Test
  fun `get nonexistent data`() {
    dao.get(randomString()).test().expectComplete().verify()
  }
}