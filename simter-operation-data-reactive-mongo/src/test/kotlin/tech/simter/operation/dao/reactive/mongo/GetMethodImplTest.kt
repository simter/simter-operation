package tech.simter.operation.dao.reactive.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
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
@DataMongoTest
class GetMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  private fun randomOperation(): Operation {
    return Operation(id = UUID.randomUUID().toString())
  }


  @Test
  fun `Get existent data`() {
    // init data
    val expected = repository.save(randomOperation()).block()!!

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