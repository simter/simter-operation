package tech.simter.operation.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import tech.simter.operation.OPERATION_READ
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationService
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationBatch
import tech.simter.reactive.security.ModuleAuthorizer

/**
 * Test [OperationServiceImpl.findByBatch].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindByBatchMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val batch = randomOperationBatch()
    val operation1 = randomOperation(batch = batch)
    val operation2 = randomOperation(batch = batch)
    every { dao.findByBatch(batch) } returns Flux.just(operation1, operation2)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findByBatch(batch)
      .test()
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
    verify(exactly = 1) { dao.findByBatch(batch) }
  }

  @Test
  fun `found nothing`() {
    // mock
    val batch = randomOperationBatch()
    every { dao.findByBatch(batch) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findByBatch(batch).test().verifyComplete()
    verify(exactly = 1) { dao.findByBatch(batch) }
  }
}