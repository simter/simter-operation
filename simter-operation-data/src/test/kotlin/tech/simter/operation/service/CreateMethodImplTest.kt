package tech.simter.operation.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.operation.OPERATION_CREATE
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.service.TestHelper.randomOperation
import tech.simter.reactive.security.ModuleAuthorizer

/**
 * Test [OperationServiceImpl.create].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@MockkBean(OperationDao::class, ModuleAuthorizer::class)
class CreateMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `create one`() {
    // mock
    val po = randomOperation()
    every { dao.create(po) } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_CREATE) } returns Mono.empty()

    // invoke and verify
    service.create(po).test().verifyComplete()
    verify(exactly = 1) { dao.create(po) }
  }
}