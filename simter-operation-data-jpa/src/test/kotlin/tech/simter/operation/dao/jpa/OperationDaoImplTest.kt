package tech.simter.operation.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.jpa.PoUtil.Companion.randomOperation
import tech.simter.operation.dao.jpa.PoUtil.Companion.randomString
import java.time.OffsetDateTime

/**
 * Test [OperationDaoImpl]
 *
 * @author zh
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
internal class OperationDaoImplTest @Autowired constructor(
  private val repository: OperationJpaRepository,
  private val dao: OperationDao
) {

  @Test
  fun get() {
  }

  @Test
  fun findByClusterFound() {
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

    // invoke
    val result = dao.findByCluster(cluster)

    // verify
    StepVerifier.create(result)
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
  }

  @Test
  fun findByClusterNothing() {
    StepVerifier.create(dao.findByCluster(randomString())).verifyComplete()
  }

  @Test
  fun findByClusterNotFound() {
    // init data
    repository.run {
      save(randomOperation(cluster = randomString()))
      save(randomOperation(cluster = randomString()))
      saveAndFlush(randomOperation())
    }

    // invoke
    val result = dao.findByCluster(randomString())

    // verify
    StepVerifier.create(result).verifyComplete()
  }

  @Test
  fun create() {
    // init data
    val operations = List(5) { randomOperation(cluster = randomString()) }

    // 1. create one, invoke and verify
    StepVerifier.create(dao.create(operations[0])).verifyComplete()
    assertEquals(operations[0], repository.getOne(operations[0].id))

    // 2. create some, invoke and verify
    repository.deleteAll()
    val operationArray = operations.toTypedArray()
    StepVerifier.create(dao.create(*operationArray)).verifyComplete()
    operations.forEach {
      assertEquals(it, repository.getOne(it.id))
    }
  }
}