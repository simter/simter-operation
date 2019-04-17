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
import tech.simter.operation.service.TestHelper.randomString

/**
 * Test [OperationServiceImpl.get].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(OperationServiceImpl::class)
@MockkBean(OperationDao::class)
class GetMethodImplTest @Autowired constructor(
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `get existent data`() {
    // mock
    val data = randomOperation()
    every { dao.get(data.id) } returns Mono.just(data)

    // invoke and verify
    service.get(data.id).test().expectNext(data).verifyComplete()
    verify(exactly = 1) {
      dao.get(data.id)
    }
  }

  @Test
  fun `get nonexistent data`() {
    // mock
    val id = randomString()
    every { dao.get(id) } returns Mono.empty()

    // invoke and verify
    service.get(id).test().expectComplete().verify()
    verify(exactly = 1) {
      dao.get(id)
    }
  }
}