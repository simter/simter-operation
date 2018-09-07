package tech.simter.operation.po

import javax.persistence.Embeddable

/**
 * The Operator.
 *
 * @author RJ
 */
@Embeddable
data class Operator(
  val id: String,
  val name: String
)