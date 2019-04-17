package tech.simter.operation.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.service.TestHelper.randomOperation
import tech.simter.operation.service.TestHelper.randomString

/**
 * Test [OperationServiceImpl.findByCluster].
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(OperationServiceImpl::class)
@MockkBean(OperationDao::class)
class FindByClusterMethodImplTest @Autowired constructor(
  private val service: OperationService,
  private val dao: OperationDao
) {
  @Test
  fun `find something`() {
    // mock
    val cluster = randomString()
    val operation1 = randomOperation(cluster)
    val operation2 = randomOperation(cluster)
    every { dao.findByCluster(cluster) } returns Flux.just(operation1, operation2)

    // invoke and verify
    service.findByCluster(cluster)
      .test()
      .expectNext(operation1)
      .expectNext(operation2)
      .verifyComplete()
    verify(exactly = 1) {
      dao.findByCluster(cluster)
    }
  }

  @Test
  fun `find nothing`() {
    // mock
    every { dao.findByCluster(any()) } returns Flux.empty()

    // invoke and verify
    service.findByCluster(randomString()).test().verifyComplete()
    verify(exactly = 1) {
      dao.findByCluster(any())
    }
  }
}