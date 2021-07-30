package tech.simter.operation.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import tech.simter.operation.OPERATION_READ
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationService
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationId
import tech.simter.reactive.security.ModuleAuthorizer

/**
 * Test [OperationServiceImpl.get].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class GetTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `get existent data`() {
    // mock
    val data = randomOperation()
    every { dao.get(data.id) } returns Mono.just(data)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.get(data.id).test().expectNext(data).verifyComplete()
    verify(exactly = 1) { dao.get(data.id) }
  }

  @Test
  fun `get nonexistent data`() {
    // mock
    val id = randomOperationId()
    every { dao.get(id) } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.get(id).test().expectComplete().verify()
    verify(exactly = 1) { dao.get(id) }
  }
}