package tech.simter.operation.dao.reactive.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.reactive.mongo.TestHelper.randomOperation
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationDaoImpl.get].
 *
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataMongoTest
class GetMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `get existent data without items`() {
    // init data
    val expected = repository.save(randomOperation()).block()!!

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get existent data with items`() {
    // init data
    // init data
    val expected = repository.save(randomOperation().apply {
      addItem(TestHelper.randomOperationItem(id = "field1"))
      addItem(TestHelper.randomOperationItem(id = "field2"))
    }).block()!!

    // invoke and verify
    dao.get(expected.id).test().expectNext(expected).verifyComplete()
  }


  @Test
  fun `get nonexistent data`() {
    dao.get(randomString()).test().expectComplete().verify()
  }
}