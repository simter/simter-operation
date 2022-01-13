package tech.simter.operation.impl.dao.mongo.po

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import tech.simter.operation.TABLE_OPERATION
import tech.simter.operation.core.Operation
import java.time.OffsetDateTime
import java.util.*

/**
 * @author RJ
 */
@Document(collection = TABLE_OPERATION)
data class OperationPo(
  @Id
  private val id: String = UUID.randomUUID().toString(),
  override val ts: OffsetDateTime = OffsetDateTime.now(),
  override val type: String,
  override val operatorId: String,
  override val operatorName: String,
  override val targetId: String,
  override val targetType: String,
  override val title: String? = null,
  override val remark: String? = null,
  override val result: String? = null,
  override val batch: String? = null,
  override val items: Set<Operation.Item.Impl> = emptySet()
) : Operation {
  override fun getId() = id
  override fun isNew() = true

  companion object {
    fun from(operation: Operation): OperationPo {
      return OperationPo(
        id = operation.id,
        ts = operation.ts,
        type = operation.type,
        operatorId = operation.operatorId,
        operatorName = operation.operatorName,
        targetId = operation.targetId,
        targetType = operation.targetType,
        title = operation.title,
        remark = operation.remark,
        result = operation.result,
        batch = operation.batch,
        items = operation.items.map { Operation.Item.Impl.from(it) }.toSet()
      )
    }
  }
}