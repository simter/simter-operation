package tech.simter.operation.dao.reactive.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.reactive.mongo.PoUtil.Companion.randomOperation
import tech.simter.operation.dao.reactive.mongo.PoUtil.Companion.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl]
 *
 * @author zh
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataMongoTest
internal class OperationDaoImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {

  @Test
  fun findByCluster() {
    // init data
    val cluster = randomString()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(cluster = cluster, offsetDateTime = now)
    val operation2 = randomOperation(cluster = cluster, offsetDateTime = now.minusHours(1))

    // invoke
    val result = repository
      .saveAll(listOf(operation1, operation2, randomOperation(cluster = randomString()), randomOperation()))
      .thenMany(dao.findByCluster(cluster))

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
    // init data and invoke
    val result = repository
      .saveAll(listOf(randomOperation(cluster = randomString()), randomOperation()))
      .thenMany(dao.findByCluster(randomString()))

    // verify
    StepVerifier.create(result).verifyComplete()
  }

  @Test
  fun create() {
    // init data
    val operations = List(5) { randomOperation(cluster = randomString()) }

    // 1. create one, invoke and verify
    StepVerifier.create(dao.create(operations[0])).verifyComplete()
    StepVerifier.create(repository.findById(operations[0].id))
      .expectNext(operations[0]).verifyComplete()

    // 2. create some, invoke and verify
    repository.deleteAll()
    val operationArray = operations.toTypedArray()
    StepVerifier.create(dao.create(*operationArray)).verifyComplete()
    operations.forEach {
      StepVerifier.create(repository.findById(it.id))
        .expectNext(it).verifyComplete()
    }
  }
}