package tech.simter.operation.impl.dao.r2dbc

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem

/**
 * Test [OperationDaoImpl.create]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class CreateMethodImplTest @Autowired constructor(
  private val dao: OperationDao
) {
  @Test
  fun `success without items`() {
    // init data
    val expected = randomOperation()

    // invoke and verify
    dao.create(expected)
      .then(dao.get(expected.id))
      .test().expectNext(expected).verifyComplete()
  }

  @Test
  fun `success with items`() {
    // init data
    val expected = randomOperation(
      items = setOf(randomOperationItem(id = "field1"), randomOperationItem(id = "field2"))
    )

    // invoke and verify
    dao.create(expected)
      .then(dao.get(expected.id))
      .test().expectNext(expected).verifyComplete()
  }
}