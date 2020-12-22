package tech.simter.operation.impl.dao.r2dbc.po

import org.springframework.data.annotation.AccessType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import tech.simter.operation.TABLE_OPERATION
import tech.simter.operation.core.Operation
import java.time.OffsetDateTime

@Table(TABLE_OPERATION)
data class OperationPo(
  @Id @JvmField
  override val id: String,
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
  val itemsCount: Short = 0.toShort()
) : Operation, Persistable<String> {
  override fun getId(): String {
    return this.id
  }

  override fun isNew(): Boolean {
    return true
  }

  companion object {
    fun from(operation: Operation, itemsCount: Short): OperationPo {
      return if (operation is OperationPo) operation
      else OperationPo(
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
        itemsCount = itemsCount
      )
    }
  }
}