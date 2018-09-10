package tech.simter.operation.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import tech.simter.operation.po.Target
import java.util.*
import kotlin.test.assertEquals

/**
 * OperationDaoImpl's test
 *
 * @author zh
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
internal class OperationDaoImplTest @Autowired constructor(
  private val repository: OperationJpaRepository,
  private val dao: OperationDao
) {
  fun getRandomOperation(cluster: String?): Operation {
    return Operation(
      type = UUID.randomUUID().toString(),
      operator = Operator(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
      target = Target(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()),
      cluster = cluster
    )
  }

  @Test
  fun get() {
  }

  @Test
  fun findByClusterFound() {
    // init data
    val cluster=UUID.randomUUID().toString()
    val operation1 = getRandomOperation(cluster)
    val operation2 = getRandomOperation(cluster)
    val operation3 = getRandomOperation(UUID.randomUUID().toString())
    val operation4 = getRandomOperation(null)
    repository.apply {
      save(operation1)
      save(operation2)
      save(operation3)
      saveAndFlush(operation4)
    }

    // invoke
    val result = dao.findByCluster(cluster)

    // verify
    StepVerifier.create(result)
      .expectNext(operation2)
      .expectNext(operation1)
      .verifyComplete()
  }

  @Test
  fun findByClusterNothing() {
    StepVerifier.create(dao.findByCluster(UUID.randomUUID().toString())).verifyComplete()
  }

  @Test
  fun findByClusterNotFound() {
    // init data
    val operation1 = getRandomOperation(UUID.randomUUID().toString())
    val operation2 = getRandomOperation(UUID.randomUUID().toString())
    val operation3 = getRandomOperation(null)
    repository.apply {
      save(operation1)
      save(operation2)
      saveAndFlush(operation3)
    }

    // invoke
    val result = dao.findByCluster(UUID.randomUUID().toString())

    // verify
    StepVerifier.create(result).verifyComplete()
  }
}