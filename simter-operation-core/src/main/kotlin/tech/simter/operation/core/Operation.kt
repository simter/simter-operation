package tech.simter.operation.core

import java.time.OffsetDateTime
import java.util.*

/**
 * The Operation.
 *
 * For indicate "someone done something to something on a time".
 * For some special case, record "what result of this operation ?".
 *
 * @author RJ
 */
interface Operation {
  val id: String

  /** The operated date-time */
  val ts: OffsetDateTime

  /** The operated type. Such as Creation, Modification, Deletion */
  val type: String

  /** The operator */
  val operatorId: String
  val operatorName: String

  /** The destination of this operation done to */
  val targetId: String
  val targetType: String

  /** The general subject */
  val title: String?
    get() = null

  /** The optional operated remark */
  val remark: String?
    get() = null

  /** The optional operated result. Such as 'ok', 'not ok' */
  val result: String?
    get() = null

  /** The optional batch */
  val batch: String?
    get() = null

  /**
   * The operation items.
   *
   * Mostly use to record the details of this operation. Such as Object's property changed.
   */
  val items: Set<Item>
    get() = emptySet()

  /**
   * The operation item.
   */
  interface Item {
    val id: String

    /** item title, label or name */
    val title: String?
      get() = null

    /** value type, eg: String|JsonObject|JsonArray */
    val valueType: String

    /** The json string of old value */
    val oldValue: String?
      get() = null

    /** The json string of new value */
    val newValue: String?
      get() = null

    /** An inner immutable [Item] implementation */
    data class Impl(
      override val id: String,
      override val title: String? = id,
      override val valueType: String,
      override val oldValue: String? = null,
      override val newValue: String? = null
    ) : Item {
      companion object {
        fun from(item: Item): Impl {
          return if (item is Impl) item
          else Impl(
            id = item.id,
            title = item.title,
            valueType = item.valueType,
            oldValue = item.oldValue,
            newValue = item.newValue
          )
        }
      }
    }

    companion object {
      /** Create an immutable [Item] instance */
      fun of(
        id: String,
        title: String? = id,
        valueType: String,
        oldValue: String? = null,
        newValue: String? = null
      ): Item {
        return Impl(id, title, valueType, oldValue, newValue)
      }
    }
  }

  /**
   * The common operation type enum.
   */
  enum class Type(private val value: Short) {
    Creation(10),
    Modification(20),
    Confirmation(30),
    Rejection(40),
    Approval(50),
    Deletion(90);

    fun value(): Short {
      return value
    }

    companion object {
      fun valueOf(value: Short): Type {
        for (type in values()) {
          if (type.value() == value) return type
        }
        throw IllegalArgumentException("Unsupported Operation Type value: $value")
      }
    }
  }

  /** An inner immutable [Operation] implementation */
  data class Impl(
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
    override val batch: String? = null,
    override val items: Set<Item.Impl> = emptySet()
  ) : Operation

  companion object {
    /** Create an immutable [Operation] instance */
    fun of(
      id: String = UUID.randomUUID().toString(),
      ts: OffsetDateTime = OffsetDateTime.now(),
      type: String,
      operatorId: String,
      operatorName: String,
      targetId: String,
      targetType: String,
      title: String? = null,
      remark: String? = null,
      result: String? = null,
      batch: String? = null,
      items: Set<Item> = emptySet()
    ): Operation {
      return Impl(
        id, ts, type, operatorId, operatorName,
        targetId, targetType, title,
        remark, result, batch, items.map { Item.Impl.from(it) }.toSet()
      )
    }
  }
}