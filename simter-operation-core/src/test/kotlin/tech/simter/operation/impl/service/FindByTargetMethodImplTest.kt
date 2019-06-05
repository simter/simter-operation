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
import tech.simter.operation.impl.service.TestHelper.randomOperation
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationServiceImpl.findByTarget].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindByTargetMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val targetType = randomString()
    val targetId = randomString()
    val operation1 = randomOperation(targetType)
    val operation2 = randomOperation(targetType)
    every { dao.findByTarget(targetType, targetId) } returns Flux.just(operation1, operation2)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findByTarget(targetType, targetId)
      .test()
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
    verify(exactly = 1) { dao.findByTarget(targetType, targetId) }
  }

  @Test
  fun `found nothing`() {
    // mock
    val targetType = randomString()
    val targetId = randomString()
    every { dao.findByTarget(targetType, targetId) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findByTarget(targetType, targetId).test().verifyComplete()
    verify(exactly = 1) { dao.findByTarget(targetType, targetId) }
  }
}