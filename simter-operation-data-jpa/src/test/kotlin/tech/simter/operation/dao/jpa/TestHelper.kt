package tech.simter.operation.dao.jpa

import tech.simter.operation.po.*
import tech.simter.operation.po.Target
import tech.simter.util.RandomUtils.randomString
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * Provide public method for test.
 *
 * @author zh
 * @author RJ
 */
object TestHelper {
  fun randomOperation(
    cluster: String? = null,
    time: OffsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    attachments: List<Attachment>? = null,
    fields: List<Field>? = null
  ): Operation {
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
      ),
      ts = time,
      attachments = attachments,
      fields = fields
    )
  }
}