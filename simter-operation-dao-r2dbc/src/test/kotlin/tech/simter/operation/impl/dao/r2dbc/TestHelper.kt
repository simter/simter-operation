package tech.simter.operation.impl.dao.r2dbc

import tech.simter.operation.impl.ImmutableOperation
import tech.simter.operation.impl.ImmutableOperation.ImmutableItem
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Provide public method of test
 *
 * @author RJ
 */
object TestHelper {
  fun randomOperation(
    batch: String? = null,
    targetId: String = randomString(),
    targetType: String = randomString(),
    ts: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    items: Set<ImmutableItem> = emptySet()
  ): ImmutableOperation {
    return ImmutableOperation(
      batch = batch,
      ts = ts,
      type = randomString(),
      operatorId = randomString(),
      operatorName = randomString(),
      targetId = targetId,
      targetType = targetType,
      title = randomString(),
      items = items
    )
  }

  fun randomOperationItem(
    id: String = randomString(),
    title: String = randomString(),
    valueType: String = "String",
    oldValue: String = randomString(),
    newValue: String = randomString()
  ): ImmutableItem {
    return ImmutableItem(
      id = id,
      title = title,
      valueType = valueType,
      oldValue = oldValue,
      newValue = newValue
    )
  }
}