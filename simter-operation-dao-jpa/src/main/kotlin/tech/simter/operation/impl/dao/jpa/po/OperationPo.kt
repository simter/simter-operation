package tech.simter.operation.impl.dao.jpa.po

import tech.simter.operation.TABLE_OPERATION
import tech.simter.operation.core.Operation
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.EAGER

/**
 * The JPA Entity implementation of [Operation].
 *
 * @author RJ
 */
@Entity
@Table(name = TABLE_OPERATION)
data class OperationPo(
  @Id
  @Column(nullable = false, length = 36)
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
  override val batch: String? = null
) : Operation {
  override fun getId() = id
  override fun isNew() = true

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

  companion object {
    fun from(operation: Operation): OperationPo {
      return if (operation is OperationPo) operation
      else {
        val po = OperationPo(
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
          batch = operation.batch
        )
        operation.items.forEach { po.addItem(OperationItemPo.from(it)) }
        po
      }
    }
  }
}