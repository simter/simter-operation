package tech.simter.operation.service

import com.nhaarman.mockito_kotlin.reset
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.service.PoUtil.Companion.randomOperation
import tech.simter.operation.service.PoUtil.Companion.randomString

/**
 * Test [OperationServiceImpl]
 *
 * @author zh
 */
@SpringJUnitConfig(OperationServiceImpl::class)
@MockBean(OperationDao::class)
internal class OperationServiceImplTest @Autowired constructor(
  private val service: OperationService,
  private val dao: OperationDao
) {

  @Test
  fun get() {
  }

  @Test
  fun findByCluster() {
    // mock
    val cluster = randomString()
    val operation1 = randomOperation(cluster)
    val operation2 = randomOperation(cluster)
    `when`(dao.findByCluster(cluster)).thenReturn(Flux.just(operation1, operation2))

    // invoke
    val operations = service.findByCluster(cluster)

    // verify
    StepVerifier.create(operations)
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
  }

  @Test
  fun create() {
    // init data
    val operations = List(5) { randomOperation() }

    // 1. create one

    // mock
    `when`(dao.create(operations[0])).thenReturn(Mono.empty())

    // invoke and verify
    StepVerifier.create(service.create(operations[0])).verifyComplete()
    verify(dao).create(operations[0])

    // 2. create some

    // mock
    reset(dao)
    val operationArray = operations.toTypedArray()
    `when`(dao.create(*operationArray)).thenReturn(Mono.empty())

    // invoke and verify
    StepVerifier.create(service.create(*operationArray)).verifyComplete()
    verify(dao).create(*operationArray)
  }
}