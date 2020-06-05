package tech.simter.operation.impl.dao.jpa

import tech.simter.operation.impl.dao.jpa.po.OperationItemPo
import tech.simter.operation.impl.dao.jpa.po.OperationPo
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem
import tech.simter.operation.test.TestHelper.randomOperationItemId
import tech.simter.operation.test.TestHelper.randomOperationTargetId
import tech.simter.operation.test.TestHelper.randomOperationTargetType
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
  fun randomOperationPo(
    batch: String? = null,
    targetId: String = randomOperationTargetId(),
    targetType: String = randomOperationTargetType(),
    ts: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    items: Set<OperationItemPo> = emptySet(),
    title: String = randomString(),
    operatorName: String = randomString(6)
  ): OperationPo {
    return OperationPo.from(randomOperation(
      batch = batch,
      ts = ts,
      operatorName = operatorName,
      targetId = targetId,
      targetType = targetType,
      title = title
    )).apply { items.forEach { addItem(it) } }
  }

  fun randomOperationItemPo(
    id: String = randomOperationItemId(),
    title: String = randomString(),
    valueType: String = "String",
    oldValue: String = randomString(),
    newValue: String = randomString()
  ): OperationItemPo = OperationItemPo.from(randomOperationItem(
    id = id,
    title = title,
    valueType = valueType,
    oldValue = oldValue,
    newValue = newValue
  ))
}