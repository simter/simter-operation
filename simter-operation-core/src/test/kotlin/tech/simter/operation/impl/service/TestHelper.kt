package tech.simter.operation.impl.service

import tech.simter.operation.core.Operation
import tech.simter.operation.impl.ImmutableOperation
import tech.simter.util.RandomUtils.randomString

/**
 * Provide public method for test.
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(batch: String? = null): Operation {
    return ImmutableOperation(
      batch = batch,
      type = randomString(),
      operatorId = randomString(),
      operatorName = randomString(),
      targetId = randomString(),
      targetType = randomString(),
      title = randomString()
    )
  }
}