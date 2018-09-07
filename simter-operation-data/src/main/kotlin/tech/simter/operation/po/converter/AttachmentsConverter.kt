package tech.simter.operation.po.converter

import tech.simter.operation.po.Attachment
import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonObjectBuilder
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * An Implementation of [Attachment] List [AttributeConverter].
 *
 * @author RJ
 */
@Converter(autoApply = true)
class AttachmentsConverter : EntitiesStringConverter<Attachment>() {
  override fun jsonToEntity(it: JsonObject): Attachment {
    return Attachment(
      id = it.getString("id"),
      name = it.getString("name"),
      ext = it.getString("ext"),
      size = it.getInt("size")
    )
  }

  override fun entityToJson(attachment: Attachment): JsonObjectBuilder {
    return Json.createObjectBuilder()
      .add("id", attachment.id)
      .add("name", attachment.name)
      .add("ext", attachment.ext)
      .add("size", attachment.size)
  }
}