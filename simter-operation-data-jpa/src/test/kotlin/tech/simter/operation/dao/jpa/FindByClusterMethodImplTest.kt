package tech.simter.operation.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.jpa.TestHelper.randomOperation
import tech.simter.operation.dao.jpa.TestHelper.randomString
import java.time.OffsetDateTime

/**
 * Test [OperationDaoImpl.findByCluster]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataJpaTest
class FindByClusterMethodImplTest @Autowired constructor(
  private val repository: OperationJpaRepository,
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
    // init data
    val cluster = randomString()
    val now = OffsetDateTime.now()
    val operation1 = randomOperation(cluster = cluster, offsetDateTime = now)
    val operation2 = randomOperation(cluster = cluster, offsetDateTime = now.minusHours(1))
    repository.run {
      save(operation1)
      save(operation2)
      save(randomOperation(cluster = randomString()))
      saveAndFlush(randomOperation())
    }

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
    repository.run {
      save(randomOperation(cluster = randomString()))
      save(randomOperation(cluster = randomString()))
      saveAndFlush(randomOperation())
    }

    // invoke and verify
    dao.findByCluster(randomString()).test().verifyComplete()
  }
}