package tech.simter.operation.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.jpa.TestHelper.randomOperation
import tech.simter.operation.po.Operation
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationDaoImpl.create].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class CreateMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `create one`() {
    // do create
    val po = randomOperation(cluster = randomString())
    dao.create(po).test().verifyComplete()

    // verify created
    assertEquals(po, rem.find(Operation::class.java, po.id).get())
  }

  @Test
  fun `create many`() {
    // do create
    val pos = List(3) { randomOperation(cluster = randomString()) }
    dao.create(*pos.toTypedArray()).test().verifyComplete()

    // verify created
    val actual = rem.queryList { em ->
      em.createQuery("select o from Operation o where o.id in :ids", Operation::class.java)
        .setParameter("ids", pos.map { it.id })
    }
    assertEquals(pos.size, actual.size)
    pos.forEach { po -> assertEquals(po, actual.first { it.id == po.id }) }
  }
}