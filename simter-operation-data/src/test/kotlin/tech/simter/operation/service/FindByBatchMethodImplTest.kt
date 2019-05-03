package tech.simter.operation.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.operation.OPERATION_READ
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.service.TestHelper.randomOperation
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationServiceImpl.findByBatch].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@MockkBean(OperationDao::class, ModuleAuthorizer::class)
class FindByBatchMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val batch = randomString()
    val operation1 = randomOperation(batch)
    val operation2 = randomOperation(batch)
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
    val batch = randomString()
    every { dao.findByBatch(batch) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findByBatch(batch).test().verifyComplete()
    verify(exactly = 1) { dao.findByBatch(batch) }
  }
}