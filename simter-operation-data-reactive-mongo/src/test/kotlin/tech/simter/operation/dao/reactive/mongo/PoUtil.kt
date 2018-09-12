package tech.simter.operation.dao.reactive.mongo

import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import tech.simter.operation.po.Target
import java.time.OffsetDateTime
import java.util.*

/**
 * provide public method of test
 *
 * @author zh
 */
class PoUtil {
  companion object {
    fun randomOperation(cluster: String? = null, offsetDateTime: OffsetDateTime = OffsetDateTime.now()): Operation {
      return Operation(
        type = randomString(),
        operator = Operator(randomString(), randomString()),
        target = Target(randomString(), randomString(), randomString()),
        cluster = cluster,
        time = offsetDateTime
      )
    }

    fun randomString(): String {
      return UUID.randomUUID().toString()
    }
  }
}