package tech.simter.operation.dao.reactive.mongo

import tech.simter.operation.po.Operation
import tech.simter.operation.po.Operator
import tech.simter.operation.po.Target
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * provide public method of test
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(
    cluster: String? = null,
    offsetDateTime: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
  ): Operation {
    return Operation(
      cluster = cluster,
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
      time = offsetDateTime
    )
  }

  fun randomString(): String {
    return UUID.randomUUID().toString()
  }
}