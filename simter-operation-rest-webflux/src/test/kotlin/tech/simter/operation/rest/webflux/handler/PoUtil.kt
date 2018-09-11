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
    fun randomOperation(): Operation {
      return Operation(
        type = UUID.randomUUID().toString(),
        operator = Operator(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
        target = Target(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())
      )
    }

    fun randomOperation(cluster: String?): Operation {
      return Operation(
        type = UUID.randomUUID().toString(),
        operator = Operator(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
        target = Target(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()),
        cluster = cluster
      )
    }

    fun randomString(): String {
      return UUID.randomUUID().toString()
    }
  }
}