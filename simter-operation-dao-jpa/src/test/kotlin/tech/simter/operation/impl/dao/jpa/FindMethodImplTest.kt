package tech.simter.operation.impl.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.operation.core.OperationDao
import tech.simter.operation.impl.dao.jpa.TestHelper.randomOperationPo
import tech.simter.operation.test.TestHelper.randomOperationBatch
import tech.simter.operation.test.TestHelper.randomOperationTargetId
import tech.simter.operation.test.TestHelper.randomOperationTargetType
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.util.AssertUtils.assertSamePropertyHasSameValue
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Test [OperationDaoImpl.find]
 *
 * @author xz
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class FindMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  private val dao: OperationDao
) {
  @BeforeEach
  private fun clean() {
    rem.executeUpdate { it.createQuery("delete from OperationPo") }
  }

  @Test
  fun `success by batches`() {
    // init data
    val batch1 = randomOperationBatch()
    val batch2 = randomOperationBatch()
    val batches = listOf(batch1, batch2)
    val emptyBatches = emptyList<String>()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperationPo(batch = batch1, ts = now)
    val operation2 = randomOperationPo(batch = batch2, ts = now.minusHours(1))
    val operation3 = randomOperationPo(batch = randomString(), ts = now.minusHours(2))
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    // batches is empty
    dao.find(batches = emptyBatches).test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(3, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
        assertSamePropertyHasSameValue(page.content[2], operation3)
      }.verifyComplete()

    // batches is not empty
    dao.find(batches = batches).test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(2, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
      }.verifyComplete()
  }

  @Test
  fun `success by targetTypes`() {
    // init data
    val targetType1 = randomOperationTargetType()
    val targetType2 = randomOperationTargetId()
    val targetTypes = listOf(targetType1, targetType2)
    val emptyTargetType = emptyList<String>()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperationPo(targetType = targetType1, ts = now)
    val operation2 = randomOperationPo(targetType = targetType2, ts = now.minusHours(1))
    val operation3 = randomOperationPo(targetType = randomString(), ts = now.minusHours(2))
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    // targetTypes is empty
    dao.find(targetTypes = emptyTargetType).test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(3, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
        assertSamePropertyHasSameValue(page.content[2], operation3)
      }.verifyComplete()

    // targetTypes is not empty
    dao.find(targetTypes = targetTypes).test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(2, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
      }.verifyComplete()
  }

  @Test
  fun `success by targetIds`() {
    // init data
    val targetId1 = randomOperationTargetId()
    val targetId2 = randomOperationTargetId()
    val targetIds = listOf(targetId1, targetId2)
    val emptyTargetId = emptyList<String>()
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperationPo(targetId = targetId1, ts = now)
    val operation2 = randomOperationPo(targetId = targetId2, ts = now.minusHours(1))
    val operation3 = randomOperationPo(targetId = randomString(), ts = now.minusHours(2))
    rem.persist(operation1, operation2, operation3)

    // invoke and verify
    // targetIds is empty
    dao.find(targetIds = emptyTargetId).test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(3, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
        assertSamePropertyHasSameValue(page.content[2], operation3)
      }.verifyComplete()

    // targetIds is not empty
    dao.find(targetIds = targetIds).test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(2, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
      }.verifyComplete()
  }

  @Test
  fun `success by search`() {
    // init data
    val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val operation1 = randomOperationPo(title = "jack and tom", ts = now)
    val operation2 = randomOperationPo(operatorName = "jack and rose", ts = now.minusHours(1))
    val operation3 = randomOperationPo(batch = "i and you", ts = now.minusHours(2))
    val operation4 = randomOperationPo(targetType = "you and she", ts = now.minusHours(3))
    rem.persist(operation1, operation2, operation3, operation4)

    // invoke and verify
    // match with title, operatorName
    dao.find(search = "jack").test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(2, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
      }.verifyComplete()

    // match with batch, targetType
    dao.find(search = "you").test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(2, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation3)
        assertSamePropertyHasSameValue(page.content[1], operation4)
      }.verifyComplete()

    // match with title, operatorName, batch and targetType
    dao.find(search = "and").test()
      .consumeNextWith { page ->
        assertEquals(0, page.number)
        assertEquals(25, page.size)
        assertEquals(4, page.totalElements)
        assertSamePropertyHasSameValue(page.content[0], operation1)
        assertSamePropertyHasSameValue(page.content[1], operation2)
        assertSamePropertyHasSameValue(page.content[2], operation3)
        assertSamePropertyHasSameValue(page.content[3], operation4)
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