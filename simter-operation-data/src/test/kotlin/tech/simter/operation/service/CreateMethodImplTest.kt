package tech.simter.operation.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.service.TestHelper.randomOperation

/**
 * Test [OperationServiceImpl.create].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(OperationServiceImpl::class)
@MockkBean(OperationDao::class)
class CreateMethodImplTest @Autowired constructor(
  private val service: OperationService,
  private val dao: OperationDao
) {
  @Test
  fun `create one`() {
    // mock
    val po = randomOperation()
    every { dao.create(po) } returns Mono.empty()

    // invoke and verify
    service.create(po).test().verifyComplete()
    verify(exactly = 1) {
      dao.create(po)
    }
  }

  @Test
  fun `create many`() {
    // mock
    val pos = List(5) { randomOperation() }.toTypedArray()
    every { dao.create(*pos) } returns Mono.empty()

    // invoke and verify
    service.create(*pos).test().verifyComplete()
    verify(exactly = 1) {
      dao.create(*pos)
    }
  }
}