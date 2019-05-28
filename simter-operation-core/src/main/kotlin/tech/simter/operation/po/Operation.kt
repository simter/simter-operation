package tech.simter.operation.po

import org.springframework.data.mongodb.core.mapping.Document
import tech.simter.operation.TABLE_OPERATION
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.EAGER
import javax.persistence.FetchType.LAZY

/**
 * The Operation.
 *
 * For record "someone do something to something sometimes".
 * For some special case, record "what result of this operation ?".
 *
 * @author RJ
 */
@Entity
@Table(name = TABLE_OPERATION)
@Document(collection = TABLE_OPERATION)
data class Operation(
  /** UUID */
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @Column(nullable = false, length = 36)
  val id: String = UUID.randomUUID().toString(),

  /** The operated date-time */
  val ts: OffsetDateTime = OffsetDateTime.now(),
  /** The operated type. Such as Creation, Modification, Deletion */
  val type: String,

  /** The operator */
  val operatorId: String,
  val operatorName: String,

  /** The destination of this operation done to */
  val targetId: String,
  val targetType: String,

  /** The general subject */
  val title: String,

  /** The optional operated remark */
  val remark: String? = null,
  /** The optional operated result. Such as ok, not ok */
  val result: String? = null,
  /** The optional group */
  val batch: String? = null
) {
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
  var items: Set<OperationItem> = mutableSetOf()
    private set(value) { // mongo need it
      field = value
    }

  fun addItem(item: OperationItem): Operation {
    if ((items as MutableSet<OperationItem>).add(item)) {
      item.parent = this
      itemsCount++
    }
    return this
  }

  fun removeItem(item: OperationItem): Operation {
    if ((items as MutableSet<OperationItem>).remove(item)) itemsCount--
    return this
  }
}