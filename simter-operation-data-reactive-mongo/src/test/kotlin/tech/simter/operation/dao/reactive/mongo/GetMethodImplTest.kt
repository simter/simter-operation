package tech.simter.operation.dao.reactive.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import java.util.*

fun randomString() = UUID.randomUUID().toString()

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
    StepVerifier.create(dao.get(randomString()))
      .expectComplete()
      .verify()
  }
}