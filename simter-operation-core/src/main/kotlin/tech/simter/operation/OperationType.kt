package tech.simter.operation

/**
 * The common operation type enum.
 *
 * @author zh
 */
enum class OperationType(private val value: Short) {
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
    fun valueOf(value: Short): OperationType {
      for (type in OperationType.values()) {
        if (type.value() == value) return type
      }
      throw IllegalArgumentException("unsupported Operation-Type value: $value")
    }
  }
}