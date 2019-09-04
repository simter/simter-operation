package tech.simter.operation.impl.dao.r2dbc

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.r2dbc.TestHelper.randomOperation
import tech.simter.operation.impl.dao.r2dbc.TestHelper.randomOperationItem
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationDaoImplByR2dbcClient.get].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class GetMethodImplTest @Autowired constructor(
  private val dao: OperationDao
) {
  @Test
  fun `get existent data without items`() {
    // init data
    val expected = randomOperation()

    // invoke and verify
    dao.create(expected)
      .then(dao.get(expected.id))
      .test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get existent data with items`() {
    // init data
    val expected = randomOperation(
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    )

    // invoke and verify
    dao.create(expected)
      .then(dao.get(expected.id))
      .test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `get nonexistent data`() {
    dao.get(randomString()).test().expectComplete().verify()
  }
}