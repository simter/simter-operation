package tech.simter.operation.core

import org.springframework.data.annotation.Id
import java.time.OffsetDateTime
import java.util.*

/**
 * The Operation view information.
 *
 * @author xz
 */
interface OperationView {
  val id: String
  val type: String
  val ts: OffsetDateTime
  val title: String?
  val result: String?
  val remark: String?
  val operatorName: String
  val targetType: String
  val targetId: String
  val batch: String?

  data class Impl(
    @Id
    override val id: String,
    override val type: String,
    override val ts: OffsetDateTime,
    override val title: String?,
    override val result: String?,
    override val remark: String?,
    override val operatorName: String,
    override val targetType: String,
    override val targetId: String,
    override val batch: String?
  ) : OperationView

  companion object {
    /** Create an immutable [OperationView] instance */
    fun of(
      id: String = UUID.randomUUID().toString(),
      ts: OffsetDateTime = OffsetDateTime.now(),
      type: String,
      operatorName: String,
      targetId: String,
      targetType: String,
      title: String? = null,
      remark: String? = null,
      result: String? = null,
      batch: String? = null
    ): OperationView {
      return Impl(
        id = id, ts = ts, type = type, operatorName = operatorName,
        targetId = targetId, targetType = targetType, title = title,
        remark = remark, result = result, batch = batch
      )
    }
  }
}