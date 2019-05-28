package tech.simter.operation.service

import tech.simter.operation.po.Operation
import tech.simter.util.RandomUtils.randomString

/**
 * Provide public method for test.
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(batch: String? = null): Operation {
    return Operation(
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