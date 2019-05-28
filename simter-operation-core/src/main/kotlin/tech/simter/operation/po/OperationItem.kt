package tech.simter.operation.po

import tech.simter.operation.TABLE_OPERATION_ITEM
import javax.persistence.*

/**
 * The OperationItem.
 *
 * @author RJ
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = ["parent", "parent\$simter_operation_data"])
@Entity
@Table(name = TABLE_OPERATION_ITEM)
@IdClass(OperationItem.GlobalId::class)
data class OperationItem(
  @Id
  val id: String,
  /** item title, label or name */
  val title: String = id,
  /** value type, eg: String|JsonObject|JsonArray */
  val valueType: String,
  /** old value */
  val oldValue: String? = null,
  /** new value */
  val newValue: String?
) {
  @org.springframework.data.annotation.Transient
  @Id
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "pid", nullable = false)
  internal lateinit var parent: Operation

  @Embeddable
  data class GlobalId(
    val parent: String,
    val id: String
  ) : java.io.Serializable
}