package tech.simter.operation.impl.dao.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.mongo.TestHelper.randomOperationItemPo
import tech.simter.operation.impl.dao.mongo.TestHelper.randomOperationPo

/**
 * Test [OperationDaoImpl.create]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class CreateMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `success without items`() {
    // init data
    val po = randomOperationPo()

    // invoke and verify
    dao.create(po).test().verifyComplete()
    repository.findById(po.id).test().expectNext(po).verifyComplete()
  }

  @Test
  fun `success with items`() {
    // do create
    val po = randomOperationPo(
      items = setOf(randomOperationItemPo(id = "field1"), randomOperationItemPo(id = "field2"))
    )
    dao.create(po).test().verifyComplete()

    // verify created
    repository.findById(po.id).test().expectNext(po).verifyComplete()
  }
}