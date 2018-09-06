package tech.simter.operation.po

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * The PO.
 *
 * @author RJ
 */
@Entity
@Table(name = "st_operation")
@Document(collection = "st_operation")
data class Operation(
  /** UUID */
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @Column(nullable = false, length = 36)
  val id: String = UUID.randomUUID().toString()
)