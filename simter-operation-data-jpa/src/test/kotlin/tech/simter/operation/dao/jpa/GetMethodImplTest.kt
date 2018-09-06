package tech.simter.operation.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation
import java.util.*

/**
 * Test [OperationDaoImpl.get].
 *
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
class GetMethodImplTest @Autowired constructor(
  private val repository: OperationJpaRepository,
  private val dao: OperationDao
) {
  private fun randomOperation(): Operation {
    return Operation(id = UUID.randomUUID().toString())
  }

  @Test
  fun `Get existent data`() {
    // init data
    val expected = repository.saveAndFlush(randomOperation())

    // invoke
    val actual = dao.get(expected.id)

    // verify
    StepVerifier.create(actual)
      .expectNext(expected)
      .verifyComplete()
  }

  @Test
  fun `Get nonexistent data`() {
    StepVerifier.create(dao.get(UUID.randomUUID().toString()))
      .expectComplete()
      .verify()
  }
}