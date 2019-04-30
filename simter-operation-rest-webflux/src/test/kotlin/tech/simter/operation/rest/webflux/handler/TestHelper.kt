package tech.simter.operation.rest.webflux.handler

import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import tech.simter.operation.po.Target
import java.util.*

/**
 * provide public method of test
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(cluster: String? = null): Operation {
    return Operation(
      type = randomString(),
      operator = Operator(
        id = randomString(),
        name = randomString()
      ),
      target = Target(
        id = randomString(),
        type = randomString(),
        name = randomString()
      ),
      batch = cluster
    )
  }

  fun randomString(): String {
    return UUID.randomUUID().toString()
  }
}