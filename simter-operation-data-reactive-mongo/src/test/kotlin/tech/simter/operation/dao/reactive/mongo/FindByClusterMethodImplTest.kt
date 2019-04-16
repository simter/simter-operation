package tech.simter.operation.dao.reactive.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.reactive.mongo.TestHelper.randomOperation
import tech.simter.operation.dao.reactive.mongo.TestHelper.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl.findByCluster]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataMongoTest
class FindByClusterMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
    // init data
    val cluster = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(cluster = cluster, offsetDateTime = now)
    val operation2 = randomOperation(cluster = cluster, offsetDateTime = now.minusHours(1))
    repository
      .saveAll(listOf(operation1, operation2, randomOperation(cluster = randomString()), randomOperation()))
      .then().test().verifyComplete()

    // invoke and verify
    dao.findByCluster(cluster).test()
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
  }

  @Test
  fun `find nothing 1`() {
    dao.findByCluster(randomString()).test().verifyComplete()
  }

  @Test
  fun `find nothing 2`() {
    // init data
    repository
      .saveAll(listOf(randomOperation(cluster = randomString()), randomOperation()))
      .then().test().verifyComplete()

    // invoke and verify
    dao.findByCluster(randomString()).test().verifyComplete()
  }
}