package tech.simter.operation.rest.webflux.handler

import tech.simter.operation.core.Operation
import tech.simter.operation.impl.ImmutableOperation
import tech.simter.operation.impl.ImmutableOperation.ImmutableItem
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * provide public method of test
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
    items: Set<Operation.Item> = emptySet()
  ): Operation {
    return ImmutableOperation(
      batch = batch,
      ts = ts,
      type = randomString(),
      operatorId = randomString(),
      operatorName = randomString(),
      targetId = targetId,
      targetType = targetType,
      title = randomString(),
      items = items.map { ImmutableItem.from(it) }.toSet()
    )
  }

  fun randomOperationItem(
    id: String = randomString(),
    title: String = randomString(),
    valueType: String = "String",
    oldValue: String = randomString(),
    newValue: String = randomString()
  ): Operation.Item {
    return ImmutableItem(
      id = id,
      title = title,
      valueType = valueType,
      oldValue = oldValue,
      newValue = newValue
    )
  }
}