package tech.simter.operation.dao.jpa

import tech.simter.operation.po.*
import tech.simter.operation.po.Target
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Provide public method for test.
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(
    cluster: String? = null,
    offsetDateTime: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    attachments: List<Attachment>? = null,
    fields: List<Field>? = null
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
      time = offsetDateTime,
      attachments = attachments,
      fields = fields
    )
  }

  fun randomString(): String {
    return UUID.randomUUID().toString()
  }
}