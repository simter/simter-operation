package tech.simter.operation.impl.dao.jpa

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperation
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationItem
import tech.simter.util.AssertUtils
import tech.simter.util.AssertUtils.assertSamePropertyHasSameValue
import tech.simter.util.RandomUtils.randomString
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl.find]
 *
 * @author xz
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class FindMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @Test
  fun `success by targetTypes`() {
    // init data
    val targetType1 = randomString()
    val targetType2 = randomString()
    val targetTypes = listOf(targetType1, targetType2)
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(targetType = targetType1, ts = now)
    val operation2 = randomOperation(targetType = targetType2, ts = now.minusHours(1))
    val operation3 = randomOperation(targetType = randomString())
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    dao.find(targetTypes = targetTypes).test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(2, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
      }.verifyComplete()
  }

  @Test
  fun `success by search`() {
    // init data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperation(title = "jack and tom", ts = now)
    val operation2 = randomOperation(title = "jack and rose", ts = now.minusHours(1))
    val operation3 = randomOperation(title = "i and you")
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    dao.find(search = "jack").test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(2, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
      }.verifyComplete()
  }

  @Test
  fun `found nothing`() {
    // invoke and verify
    dao.find().test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(0, page.totalElements)
        assertTrue(page.content.isEmpty())
      }.verifyComplete()
  }
} 