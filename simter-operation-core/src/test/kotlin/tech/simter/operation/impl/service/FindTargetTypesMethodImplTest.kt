package tech.simter.operation.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.operation.OPERATION_READ
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationService
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationServiceImpl.findTargetTypes].
 *
 * @author xz
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindTargetTypesMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `found something`() {
    // mock
    val targetType1 = randomString()
    val targetType2 = randomString()

    every { dao.findTargetTypes() } returns Flux.just(targetType1, targetType2)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findTargetTypes()
      .test()
      .expectNext(targetType1)
      .expectNext(targetType2)
      .verifyComplete()
    verify(exactly = 1) { dao.findTargetTypes() }
  }

  @Test
  fun `found nothing`() {
    every { dao.findTargetTypes() } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findTargetTypes().test().verifyComplete()
    verify(exactly = 1) { dao.findTargetTypes() }
  }
}