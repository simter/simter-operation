package tech.simter.operation.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import java.util.*

fun randomString() = UUID.randomUUID().toString()

@SpringJUnitConfig(OperationServiceImpl::class)
@MockBean(OperationDao::class)
class GetMethodImplTest @Autowired constructor(
  private val dao: OperationDao,
  private val service: OperationService
) {
  private fun randomOperation(): Operation {
    return Operation(
      type = randomString(),
      operator = Operator(
        id = randomString(),
        name = randomString()
      ),
      target = tech.simter.operation.po.Target(
        id = randomString(),
        type = randomString(),
        name = randomString()
      )
    )
  }

  @Test
  fun `Get existent data`() {
    // mock
    val data = randomOperation()
    Mockito.`when`(dao.get(data.id)).thenReturn(Mono.just(data))

    // invoke
    val actual = service.get(data.id)

    // verify
    StepVerifier.create(actual)
      .expectNext(data)
      .verifyComplete()
    Mockito.verify(dao).get(data.id)
  }

  @Test
  fun `Get nonexistent data`() {
    // mock
    val id = randomString()
    Mockito.`when`(dao.get(id)).thenReturn(Mono.empty())

    // invoke
    val actual = service.get(id)

    // verify
    StepVerifier.create(actual)
      .expectComplete()
      .verify()
    Mockito.verify(dao).get(id)
  }
}