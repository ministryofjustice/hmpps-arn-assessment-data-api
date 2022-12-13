package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.AddressType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Target(AnnotationTarget.FIELD)
annotation class KlaxonDate

val dateConverter = object : Converter {
  override fun canConvert(cls: Class<*>) = cls == LocalDate::class.java

  override fun fromJson(jv: JsonValue) =
    jv.string?.let {
      LocalDate.parse(it, DateTimeFormatter.ISO_DATE)
    } ?: throw KlaxonException("Couldn't parse date: ${jv.string}")

  override fun toJson(value: Any) = """ "${(value as LocalDate).format(DateTimeFormatter.ISO_DATE)}" """
}

@Target(AnnotationTarget.FIELD)
annotation class KlaxonUuid

val uuidConverter = object : Converter {
  override fun canConvert(cls: Class<*>) = cls == UUID::class.java

  override fun fromJson(jv: JsonValue) =
    jv.string?.let {
      UUID.fromString(it)
    } ?: throw KlaxonException("Couldn't parse UUID: ${jv.string}")

  override fun toJson(value: Any) = """ "${(value as UUID)}" """
}

@Target(AnnotationTarget.FIELD)
annotation class KlaxonAddressType

val addressTypeConverter = object : Converter {
  override fun canConvert(cls: Class<*>) = cls == AddressType::class.java

  override fun fromJson(jv: JsonValue) =
    jv.string?.let {
      AddressType.valueOf(it)
    } ?: throw KlaxonException("Couldn't parse address type: ${jv.string}")

  override fun toJson(value: Any) = """ "${(value as AddressType)}" """
}

class JsonEventValues {
  companion object {
    val klaxon = Klaxon()
      .fieldConverter(KlaxonDate::class, dateConverter)
      .fieldConverter(KlaxonUuid::class, uuidConverter)
      .fieldConverter(KlaxonAddressType::class, addressTypeConverter)

    inline fun <reified T> deserialize(s: String): T? {
      return klaxon.parse<T>(s)
    }

    fun serialize(o: Any): String {
      return klaxon.toJsonString(o)
    }
  }
}
