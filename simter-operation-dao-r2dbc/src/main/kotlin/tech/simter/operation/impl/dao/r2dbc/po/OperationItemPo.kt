package tech.simter.operation.impl.dao.r2dbc.po

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import tech.simter.operation.TABLE_OPERATION_ITEM
import tech.simter.operation.core.Operation

@Table(TABLE_OPERATION_ITEM)
data class OperationItemPo(
  val pid: String,
  @Id
  private val id: String,
  override val title: String? = id,
  override val valueType: String,
  override val oldValue: String? = null,
  override val newValue: String? = null
) : Operation.Item {
  override fun getId(): String {
    return this.id
  }

  override fun isNew(): Boolean {
    return true
  }

  companion object {
    fun from(pid: String, item: Operation.Item): OperationItemPo {
      return if (item is OperationItemPo) item
      else OperationItemPo(
        pid = pid,
        id = item.id,
        title = item.title,
        valueType = item.valueType,
        oldValue = item.oldValue,
        newValue = item.newValue
      )
    }
  }
}