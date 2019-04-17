package tech.simter.operation.dao.reactive.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.reactive.mongo.TestHelper.randomOperation
import tech.simter.operation.dao.reactive.mongo.TestHelper.randomString

/**
 * Test [OperationDaoImpl]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataMongoTest
class CreateMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `create one`() {
    // init data
    val po = randomOperation(cluster = randomString())

    // invoke and verify
    dao.create(po).test().verifyComplete()
    repository.findById(po.id).test().expectNext(po).verifyComplete()
  }

  @Test
  fun `create many`() {
    // init data
    val operations = List(5) { randomOperation(cluster = randomString()) }.toTypedArray()

    dao.create(*operations).test().verifyComplete()
    operations.forEach {
      repository.findById(it.id).test().expectNext(it).verifyComplete()
    }
  }
}