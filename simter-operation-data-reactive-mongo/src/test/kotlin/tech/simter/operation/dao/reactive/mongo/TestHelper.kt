package tech.simter.operation.dao.reactive.mongo

import tech.simter.operation.po.Operation
import tech.simter.operation.po.OperationItem
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
    ts: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
  ): Operation {
    return Operation(
      batch = batch,
      ts = ts,
      type = randomString(),
      operatorId = randomString(),
      operatorName = randomString(),
      targetId = targetId,
      targetType = targetType,
      title = randomString()
    )
  }

  fun randomOperationItem(
    id: String = randomString(),
    title: String = randomString(),
    valueType: String = "String",
    oldValue: String = randomString(),
    newValue: String = randomString()
  ): OperationItem {
    return OperationItem(
      id = id,
      title = title,
      valueType = valueType,
      oldValue = oldValue,
      newValue = newValue
    )
  }
}