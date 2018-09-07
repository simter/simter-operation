package tech.simter.operation.po

import org.springframework.data.mongodb.core.mapping.Document
import tech.simter.operation.po.converter.AttachmentsConverter
import tech.simter.operation.po.converter.FieldsConverter
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

/**
 * The Operation.
 *
 * For record "someone do something to something sometimes".
 * For some special case, record "what result of this operation ?".
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
  val id: String = UUID.randomUUID().toString(),

  /** The operated date-time */
  val time: OffsetDateTime = OffsetDateTime.now(),
  /** The operated type. Such as creation, modification,deletion */
  val type: String,
  /** The operator */
  @Embedded
  val operator: Operator,
  /** The destination of this operation done to */
  @Embedded
  val target: Target,

  /** The general subject */
  val title: String = "${target.name} $type",

  /** The optional operated comment */
  val comment: String? = null,
  /** The optional operated result. Such as ok, not ok */
  val result: String? = null,
  /** The optional group */
  val cluster: String? = null,

  /** The optional attachments */
  @Convert(converter = AttachmentsConverter::class)
  val attachments: List<Attachment>? = null,

  /** The optional changed fields */
  @Convert(converter = FieldsConverter::class)
  val fields: List<Field>? = null
)