package tech.simter.operation.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.jpa.TestHelper.randomOperation
import tech.simter.operation.dao.jpa.TestHelper.randomString

/**
 * Test [OperationDaoImpl.create].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataJpaTest
class CreateMethodImplTest @Autowired constructor(
  private val repository: OperationJpaRepository,
  private val dao: OperationDao
) {
  @Test
  fun `create one`() {
    // init data
    val po = randomOperation(cluster = randomString())

    // invoke and verify
    dao.create(po).test().verifyComplete()
    assertEquals(po, repository.getOne(po.id))
  }

  @Test
  fun `create many`() {
    // init data
    val pos = List(5) { randomOperation(cluster = randomString()) }.toTypedArray()

    // invoke and verify
    dao.create(*pos).test().verifyComplete()
    pos.forEach {
      assertEquals(it, repository.getOne(it.id))
    }
  }
}