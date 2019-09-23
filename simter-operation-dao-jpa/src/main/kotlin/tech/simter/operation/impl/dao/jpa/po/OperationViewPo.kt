package tech.simter.operation.impl.dao.jpa.po

import tech.simter.operation.core.OperationView
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.Id

/**
 * The Operation view information.
 *
 * @author xz
 */
@Entity
data class OperationViewPo(
  @Id
  override val id: String,
  override val type: String,
  override val ts: OffsetDateTime,
  override val title: String? = null,
  override val operatorName: String,
  override val targetType: String
): OperationView