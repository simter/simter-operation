package tech.simter.operation.rest.webflux.handler

import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import tech.simter.operation.po.Target
import java.util.*

/**
 * provide public method of test
 *
 * @author zh
 */
class PoUtil {
  companion object {
    fun randomOperation(cluster: String? = null): Operation {
      return Operation(
        type = randomString(),
        operator = Operator(randomString(), randomString()),
        target = Target(randomString(), randomString(), randomString()),
        cluster = cluster
      )
    }

    fun randomString(): String {
      return UUID.randomUUID().toString()
    }
  }
}