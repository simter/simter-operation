package tech.simter.operation.impl.dao.jpa.po

import tech.simter.operation.TABLE_OPERATION
import tech.simter.operation.core.Operation
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
  @Id
  @Column(nullable = false, length = 36)
  override val id: String = UUID.randomUUID().toString(),
  override val ts: OffsetDateTime = OffsetDateTime.now(),
  override val type: String,
  override val operatorId: String,
  override val operatorName: String,
  override val targetId: String,
  override val targetType: String,
  override val title: String? = null,
  override val remark: String? = null,
  override val result: String? = null,
  override val batch: String? = null
) : Operation {
  /** Items total count */
  private var itemsCount: Short = 0.toShort()

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
}