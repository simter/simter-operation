package tech.simter.operation.impl

import tech.simter.operation.core.Operation
import tech.simter.operation.core.Operation.Item
import java.time.OffsetDateTime
import java.util.*

/**
 * The immutable implementation of [Operation].
 *
 * @author RJ
 */
data class ImmutableOperation(
  override val id: String = UUID.randomUUID().toString(),
  override val ts: OffsetDateTime = OffsetDateTime.now(),
  override val type: String,
  override val operatorId: String,
  override val operatorName: String,
  override val targetId: String,
  override val targetType: String,
  override val title: String,
  override val remark: String? = null,
  override val result: String? = null,
  override val batch: String? = null,
  override val items: Set<Item> = emptySet()
) : Operation {
  data class ImmutableItem(
    override val id: String,
    override val title: String = id,
    override val valueType: String,
    override val oldValue: String? = null,
    override val newValue: String? = null
  ) : Item
}