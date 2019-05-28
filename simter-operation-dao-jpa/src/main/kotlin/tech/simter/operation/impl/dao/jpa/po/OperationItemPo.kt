package tech.simter.operation.impl.dao.jpa.po

import tech.simter.operation.core.Operation
import tech.simter.operation.support.TABLE_OPERATION_ITEM
import javax.persistence.*

/**
 * @author RJ
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = ["parent", "parent\$simter_operation_data"])
@Entity
@Table(name = TABLE_OPERATION_ITEM)
@IdClass(OperationItemPo.GlobalId::class)
data class OperationItemPo(
  @Id
  override val id: String,
  /** item title, label or name */
  override val title: String = id,
  /** value type, eg: String|JsonObject|JsonArray */
  override val valueType: String,
  /** old value */
  override val oldValue: String? = null,
  /** new value */
  override val newValue: String? = null
) : Operation.Item {
  @org.springframework.data.annotation.Transient
  @Id
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "pid", nullable = false)
  internal lateinit var parent: OperationPo

  @Embeddable
  data class GlobalId(
    val parent: String,
    val id: String
  ) : java.io.Serializable
}