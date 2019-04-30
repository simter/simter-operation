package tech.simter.operation.service

import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import tech.simter.operation.po.Target
import java.util.*

/**
 * Provide public method for test.
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(cluster: String? = null): Operation {
    return Operation(
      batch = cluster,
      type = randomString(),
      operator = Operator(
        id = randomString(),
        name = randomString()
      ),
      target = Target(
        id = randomString(),
        type = randomString(),
        name = randomString()
      )
    )
  }

  fun randomString(): String {
    return UUID.randomUUID().toString()
  }
}