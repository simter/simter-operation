package tech.simter.operation.po

import javax.persistence.Embeddable

/**
 * The Operate Target.
 *
 * @author RJ
 */
@Embeddable
data class Target(
  val id: String,
  val type: String,
  val name: String? = null
)