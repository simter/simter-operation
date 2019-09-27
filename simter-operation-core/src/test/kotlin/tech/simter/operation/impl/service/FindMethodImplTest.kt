package tech.simter.operation.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.exception.PermissionDeniedException
import tech.simter.operation.OPERATION_READ
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationDao
import tech.simter.operation.core.OperationService
import tech.simter.operation.impl.service.TestHelper.randomOperation
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils
import tech.simter.util.RandomUtils.randomString

/**
 * Test [OperationServiceImpl.find].
 *
 * @author xz
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: OperationDao,
  private val service: OperationService
) {
  @Test
  fun `find successful`() {
    // mock
    val pageNo = 1
    val pageSize = 25
    val batches = null
    val targetTypes = listOf<String>()
    val targetIds = listOf<String>()
    val search = randomString()
    val emptyList = listOf<Operation>()
    val page = PageImpl(emptyList, PageRequest.of(pageNo - 1, pageSize), 0)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()
    every { dao.find(pageNo, pageSize, batches, targetTypes, targetIds, search) } returns Mono.just(page)

    // invoke and verify
    service.find(pageNo, pageSize, batches, targetTypes, targetIds, search).test().expectNext(page).verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.find(pageNo, pageSize, batches, targetTypes, targetIds, search)
    }
  }

  @Test
  fun `failed by PermissionDenied`() {
    // mock
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.error(PermissionDeniedException())

    // invoke and verify
    service.find().test().verifyError(PermissionDeniedException::class.java)
    verify { moduleAuthorizer.verifyHasPermission(OPERATION_READ) }
  }
}