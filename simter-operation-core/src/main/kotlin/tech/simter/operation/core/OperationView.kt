package tech.simter.operation.core

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import java.time.OffsetDateTime
import java.util.*

/**
 * The Operation view information.
 *
 * @author xz
 * @author RJ
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

  @Serializable
  @SerialName("OperationView")
  data class Impl(
    @Id
    override val id: String,
    override val type: String,
    @Contextual
    override val ts: OffsetDateTime,
    override val title: String? = null,
    override val result: String? = null,
    override val remark: String? = null,
    override val operatorName: String,
    override val targetType: String,
    override val targetId: String,
    override val batch: String? = null
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

    /** Create from an [Operation] instance */
    fun from(operation: Operation): OperationView {
      return of(
        id = operation.id,
        ts = operation.ts,
        batch = operation.batch,
        type = operation.type,
        operatorName = operation.operatorName,
        targetId = operation.targetId,
        targetType = operation.targetType,
        title = operation.title,
        remark = operation.remark,
        result = operation.result
      )
    }
  }
}