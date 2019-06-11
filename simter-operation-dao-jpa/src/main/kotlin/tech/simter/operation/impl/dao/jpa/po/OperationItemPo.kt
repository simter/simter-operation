package tech.simter.operation.impl.dao.jpa.po

import tech.simter.operation.TABLE_OPERATION_ITEM
import tech.simter.operation.core.Operation
import javax.persistence.*

/**
 * The JPA Entity implementation of [Operation.Item].
 *
 * @author RJ
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = ["parent", "parent\$simter_operation_dao_jpa"])
@Entity
@Table(name = TABLE_OPERATION_ITEM)
@IdClass(OperationItemPo.GlobalId::class)
data class OperationItemPo(
  @Id
  override val id: String,
  override val title: String? = id,
  override val valueType: String,
  override val oldValue: String? = null,
  override val newValue: String? = null
) : Operation.Item {
  @Id
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "pid", nullable = false)
  internal lateinit var parent: OperationPo

  @Embeddable
  data class GlobalId(
    val parent: String,
    val id: String
  ) : java.io.Serializable

  companion object {
    fun from(item: Operation.Item): OperationItemPo {
      return if (item is OperationItemPo) item
      else OperationItemPo(
        id = item.id,
        title = item.title,
        valueType = item.valueType,
        oldValue = item.oldValue,
        newValue = item.newValue
      )
    }
  }
}