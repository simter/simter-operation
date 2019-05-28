package tech.simter.operation.impl.dao.jpa.po

import tech.simter.operation.core.Operation
import tech.simter.operation.support.TABLE_OPERATION
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.EAGER

/**
 * @author RJ
 */
@Entity
@Table(name = TABLE_OPERATION)
data class OperationPo(
  /** UUID */
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @Column(nullable = false, length = 36)
  override val id: String = UUID.randomUUID().toString(),

  /** The operated date-time */
  override val ts: OffsetDateTime = OffsetDateTime.now(),
  /** The operated type. Such as Creation, Modification, Deletion */
  override val type: String,

  /** The operator */
  override val operatorId: String,
  override val operatorName: String,

  /** The destination of this operation done to */
  override val targetId: String,
  override val targetType: String,

  /** The general subject */
  override val title: String? = null,

  /** The optional operated remark */
  override val remark: String? = null,
  /** The optional operated result. Such as ok, not ok */
  override val result: String? = null,
  /** The optional group */
  override val batch: String? = null
) : Operation {
  /** Items total count */
  var itemsCount: Short = 0.toShort()
    private set(value) {
      field = value
    }

  /**
   * The operation detail items.
   *
   * Mostly use to record Object's property changed.
   */
  @OneToMany(mappedBy = "parent", fetch = EAGER, cascade = [ALL], orphanRemoval = true)
  @OrderBy("id asc")
  override var items: MutableSet<OperationItemPo> = mutableSetOf()
    private set(value) {
      field = value
      itemsCount = field.size.toShort()
      field.forEach { it.parent = this }
    }

  fun addItem(item: OperationItemPo): OperationPo {
    if (items.add(item)) {
      item.parent = this
      itemsCount++
    }
    return this
  }

  fun removeItem(item: OperationItemPo): OperationPo {
    if (items.remove(item)) itemsCount--
    return this
  }
}