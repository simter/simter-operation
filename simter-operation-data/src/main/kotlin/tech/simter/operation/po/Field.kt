package tech.simter.operation.po

/**
 * The Field.
 *
 * @author RJ
 */
data class Field(
  val id: String,
  val name: String = id,
  val type: String,
  val oldValue: String? = null,
  val newValue: String?
)