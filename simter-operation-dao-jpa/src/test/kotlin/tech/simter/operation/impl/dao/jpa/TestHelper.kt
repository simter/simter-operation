package tech.simter.operation.impl.dao.jpa

import tech.simter.operation.impl.dao.jpa.po.OperationItemPo
import tech.simter.operation.impl.dao.jpa.po.OperationPo
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Provide public method for test.
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(
    batch: String? = null,
    targetId: String = randomString(),
    targetType: String = randomString(),
    ts: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    items: Set<OperationItemPo> = emptySet(),
    title: String = randomString()
  ): OperationPo {
    return OperationPo(
      batch = batch,
      ts = ts,
      type = randomString(),
      operatorId = randomString(),
      operatorName = randomString(),
      targetId = targetId,
      targetType = targetType,
      title = title
    ).apply { items.forEach { addItem(it) } }
  }

  fun randomOperationItem(
    id: String = randomString(),
    title: String = randomString(),
    valueType: String = "String",
    oldValue: String = randomString(),
    newValue: String = randomString()
  ): OperationItemPo {
    return OperationItemPo(
      id = id,
      title = title,
      valueType = valueType,
      oldValue = oldValue,
      newValue = newValue
    )
  }
}