package tech.simter.operation.po.converter

import tech.simter.operation.po.Field
import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonObjectBuilder
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * An Implementation of [Field] List [AttributeConverter].
 *
 * @author RJ
 */
@Converter(autoApply = true)
class FieldsConverter : EntitiesStringConverter<Field>() {
  override fun jsonToEntity(it: JsonObject): Field {
    return Field(
      id = it.getString("id"),
      name = it.getString("name"),
      type = it.getString("type"),
      oldValue = if (it.containsKey("oldValue")) it.getString("oldValue") else null,
      newValue = if (it.containsKey("newValue")) it.getString("newValue") else null
    )
  }

  override fun entityToJson(attachment: Field): JsonObjectBuilder {
    val builder = Json.createObjectBuilder()
      .add("id", attachment.id)
      .add("name", attachment.name)
      .add("type", attachment.type)
    attachment.oldValue?.let { builder.add("oldValue", attachment.oldValue) }
    attachment.newValue?.let { builder.add("newValue", attachment.newValue) }
    return builder
  }
}