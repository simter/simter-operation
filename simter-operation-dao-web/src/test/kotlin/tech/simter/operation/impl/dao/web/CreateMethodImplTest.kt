package tech.simter.operation.impl.dao.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.web.TestHelper.randomOperation
import tech.simter.operation.impl.dao.web.TestHelper.randomOperationItem

/**
 * Test [OperationDaoImplByWebFlux.create].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
@TestPropertySource(properties = ["proxy.host=127.0.0.1", "proxy.port=8888"])
class CreateMethodImplTest @Autowired constructor(
  private val dao: OperationDao
) {
  @Test
  fun `success without items`() {
    // init data
    val expected = randomOperation()

    // invoke and verify
    dao.create(expected).test().verifyComplete()
  }

  @Test
  fun `success with items`() {
    // init data
    val expected = randomOperation(
      items = setOf(
        randomOperationItem(id = "field1", oldValue = null),
        randomOperationItem(id = "field2", newValue = null),
        randomOperationItem(id = "field3")
      )
    )

    // invoke and verify
    dao.create(expected).test().verifyComplete()
  }
}