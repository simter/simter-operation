package tech.simter.operation.po.converter

import java.io.StringReader
import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonObjectBuilder
import javax.persistence.AttributeConverter

/**
 * An Implementation of entities [AttributeConverter].
 *
 * @author RJ
 */
abstract class EntitiesStringConverter<E> : AttributeConverter<List<E>?, String?> {
  override fun convertToDatabaseColumn(attribute: List<E>?): String? {
    return attribute?.let { list ->
      val arrayBuilder = Json.createArrayBuilder()
      list.forEach { arrayBuilder.add(entityToJson(it)) }
      arrayBuilder.build().toString()
    }
  }

  override fun convertToEntityAttribute(dbData: String?): List<E>? {
    return dbData?.let { data ->
      Json.createReader(StringReader(data)).readArray().map { jsonToEntity(it as JsonObject) }
    }
  }

  abstract fun jsonToEntity(it: JsonObject): E
  abstract fun entityToJson(attachment: E): JsonObjectBuilder
}