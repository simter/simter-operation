package tech.simter.operation.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.jpa.TestHelper.randomOperation
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime

/**
 * Test [OperationDaoImpl.findByCluster]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class FindByClusterMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
    // init data
    val cluster = randomString()
    val now = OffsetDateTime.now()
    val operation1 = randomOperation(cluster = cluster, time = now)
    val operation2 = randomOperation(cluster = cluster, time = now.minusHours(1))
    val operation3 = randomOperation(cluster = randomString())
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    dao.findByCluster(cluster).test().expectNext(operation1).expectNext(operation2).verifyComplete()
  }

  @Test
  fun `find nothing 1`() {
    dao.findByCluster(randomString()).test().verifyComplete()
  }

  @Test
  fun `find nothing 2`() {
    // init data
    rem.persist(randomOperation(cluster = randomString()))

    // invoke and verify
    dao.findByCluster(randomString()).test().verifyComplete()
  }
}