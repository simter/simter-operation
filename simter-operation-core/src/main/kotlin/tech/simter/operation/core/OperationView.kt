package tech.simter.operation.core

import java.time.OffsetDateTime

/**
 * The Operation view information.
 *
 * @author xz
 */
interface OperationView {
  val id: String
  val type: String
  val ts: OffsetDateTime
  val title: String?
  val operatorName: String
  val targetType: String
  val targetId: String
  val batch: String?
}