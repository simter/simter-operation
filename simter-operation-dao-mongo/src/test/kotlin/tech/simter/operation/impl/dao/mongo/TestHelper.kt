package tech.simter.operation.impl.dao.mongo

import tech.simter.operation.core.Operation
import tech.simter.operation.impl.dao.mongo.po.OperationPo
import tech.simter.operation.test.TestHelper
import tech.simter.operation.test.TestHelper.randomOperation
import tech.simter.operation.test.TestHelper.randomOperationItem
import tech.simter.operation.test.TestHelper.randomOperationItemId
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * provide public method for test
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperationPo(
    batch: String? = null,
    targetId: String = TestHelper.randomOperationTargetId(),
    targetType: String = TestHelper.randomOperationTargetType(),
    ts: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    items: Set<Operation.Item.Impl> = emptySet()
  ): OperationPo {
    return OperationPo.from(randomOperation(
      batch = batch,
      ts = ts,
      targetId = targetId,
      targetType = targetType,
      items = items
    ))
  }

  fun randomOperationItemPo(id: String = randomOperationItemId()): Operation.Item.Impl {
    return Operation.Item.Impl.from(randomOperationItem(id = id))
  }
}