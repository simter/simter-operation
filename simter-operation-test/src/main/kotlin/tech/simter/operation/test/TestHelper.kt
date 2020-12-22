package tech.simter.operation.test

import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationView
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Some common tools for unit test.
 *
 * @author RJ
 */
object TestHelper {
  /** Create a random operation id */
  fun randomOperationId(): String {
    return randomString()
  }

  /** Create a random operation title */
  fun randomOperationTitle(prefix: String = "", suffix: String = "", randomLen: Int = 6): String {
    return prefix + randomString(len = randomLen) + suffix
  }

  /** Create a random operation batch */
  fun randomOperationBatch(len: Int = 10): String {
    return randomString(len)
  }

  /** Create a random operation type */
  fun randomOperationType(len: Int = 10): String {
    return randomString(len)
  }

  /** Create a random operation targetId */
  fun randomOperationTargetId(len: Int = 6): String {
    return randomString(len)
  }

  /** Create a random operation targetType */
  fun randomOperationTargetType(len: Int = 10): String {
    return randomString(len)
  }

  /** Create a random [Operation] instance */
  fun randomOperation(
    id: String = UUID.randomUUID().toString(),
    ts: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    batch: String? = null,
    type: String = randomOperationType(),
    operatorId: String = randomString(6),
    operatorName: String = randomString(6),
    targetId: String = randomOperationTargetId(),
    targetType: String = randomOperationTargetType(),
    title: String = randomString(),
    remark: String? = null,
    result: String? = null,
    items: Set<Operation.Item> = emptySet()
  ): Operation {
    return Operation.of(
      id = id,
      ts = ts,
      batch = batch,
      type = type,
      operatorId = operatorId,
      operatorName = operatorName,
      targetId = targetId,
      targetType = targetType,
      title = title,
      remark = remark,
      result = result,
      items = items
    )
  }

  /** Create a random [Operation] instance */
  fun randomOperationView(
    ts: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    batch: String? = null,
    type: String = randomOperationType(),
    operatorName: String = randomString(6),
    targetId: String = randomOperationTargetId(),
    targetType: String = randomOperationTargetType(),
    title: String = randomString(),
    remark: String? = null,
    result: String? = null
  ): OperationView {
    return OperationView.of(
      ts = ts,
      batch = batch,
      type = type,
      operatorName = operatorName,
      targetId = targetId,
      targetType = targetType,
      title = title,
      remark = remark,
      result = result
    )
  }

  /** Create a random operation item id */
  fun randomOperationItemId(): String {
    return randomString()
  }

  /** Create a random [Operation.Item] instance */
  fun randomOperationItem(
    id: String = randomOperationItemId(),
    title: String = randomString(),
    valueType: String = "String",
    oldValue: String? = randomString(),
    newValue: String? = randomString()
  ): Operation.Item {
    return Operation.Item.of(
      id = id,
      title = title,
      valueType = valueType,
      oldValue = oldValue,
      newValue = newValue
    )
  }
}