package tech.simter.operation.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import tech.simter.operation.po.Target
import java.util.*

/**
 * test of [OperationServiceImpl]
 *
 * @author zh
 */
@SpringJUnitConfig(OperationServiceImpl::class)
@MockBean(OperationDao::class)
internal class OperationServiceImplTest @Autowired constructor(
  private val service: OperationService,
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
  fun findByCluster() {
    // mock
    val cluster = UUID.randomUUID().toString()
    val operation1 = getRandomOperation(cluster)
    val operation2 = getRandomOperation(cluster)
    `when`(dao.findByCluster(cluster)).thenReturn(Flux.just(operation1, operation2))

    // invoke
    val operations = service.findByCluster(cluster)

    // verify
    StepVerifier.create(operations)
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
  }
}