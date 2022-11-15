package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATED_PERSON_DETAILS
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Target(AnnotationTarget.FIELD)
annotation class KlaxonDate

val dateConverter = object : Converter {
  override fun canConvert(cls: Class<*>) = cls == LocalDate::class.java

  override fun fromJson(jv: JsonValue) =
    if (jv.string != null) {
      LocalDate.parse(jv.string, DateTimeFormatter.ISO_DATE)
    } else {
      throw KlaxonException("Couldn't parse date: ${jv.string}")
    }

  override fun toJson(o: Any) = """ "${(o as LocalDate).format(DateTimeFormatter.ISO_DATE)}" """
}

@Target(AnnotationTarget.FIELD)
annotation class KlaxonUuid

val uuidConverter = object : Converter {
  override fun canConvert(cls: Class<*>) = cls == UUID::class.java

  override fun fromJson(jv: JsonValue) =
    if (jv.string != null) {
      UUID.fromString(jv.string)
    } else {
      throw KlaxonException("Couldn't parse UUID: ${jv.string}")
    }

  override fun toJson(o: Any) = """ "${(o as UUID)}" """
}

@Target(AnnotationTarget.FIELD)
annotation class KlaxonAddressType

val addressTypeConverter = object : Converter {
  override fun canConvert(cls: Class<*>) = cls == AddressType::class.java

  override fun fromJson(jv: JsonValue) =
    if (jv.string != null) {
      AddressType.valueOf(jv.string!!)
    } else {
      throw KlaxonException("Couldn't parse UUID: ${jv.string}")
    }

  override fun toJson(o: Any) = """ "${(o as AddressType)}" """
}

abstract class Event(
  open val aggregateId: UUID,
  open val values: Any,
) {
  abstract fun getType(): EventType

  fun toEventEntity(): EventEntity {
    val serializedValues = Klaxon()
      .fieldConverter(KlaxonDate::class, dateConverter)
      .fieldConverter(KlaxonUuid::class, uuidConverter)
      .fieldConverter(KlaxonAddressType::class, addressTypeConverter)
      .toJsonString(values)

    return EventEntity(
      aggregateId = aggregateId,
      eventType = getType(),
      values = serializedValues,
    )
  }
}

data class PersonDetailsValues(
  val givenName: String,
  val familyName: String,
  @KlaxonDate
  val dateOfBirth: LocalDate,
)

data class PersonAddressDetailsValues(
  @KlaxonUuid
  val addressUUID: UUID,
  @KlaxonAddressType
  val addressType: AddressType,
)

data class AddressDetailsValues(
  val building: String,
  val postcode: String,
)

class PersonCreatedEvent(
  override val aggregateId: UUID,
  override val values: PersonDetailsValues,
) : Event(aggregateId, values) {
  override fun getType() = CREATED_PERSON

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): PersonCreatedEvent {
      val personDetailsValues = Klaxon()
        .fieldConverter(KlaxonDate::class, dateConverter)
        .parse<PersonDetailsValues>(eventEntity.values)!!

      return PersonCreatedEvent(
        aggregateId = eventEntity.aggregateId,
        values = personDetailsValues,
      )
    }
  }
}

class PersonUpdatedEvent(
  override val aggregateId: UUID,
  override val values: PersonDetailsValues,
) : Event(aggregateId, values) {
  override fun getType() = UPDATED_PERSON_DETAILS

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): PersonUpdatedEvent {
      val personDetailsValues = Klaxon()
        .fieldConverter(KlaxonDate::class, dateConverter)
        .parse<PersonDetailsValues>(eventEntity.values)!!

      return PersonUpdatedEvent(
        aggregateId = eventEntity.aggregateId,
        values = personDetailsValues,
      )
    }
  }
}

class ApprovedPersonChangesEvent(
  override val aggregateId: UUID,
) : Event(aggregateId, emptyMap<String, String>()) {
  override fun getType() = CHANGES_APPROVED

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): ApprovedPersonChangesEvent {
      return ApprovedPersonChangesEvent(
        aggregateId = eventEntity.aggregateId,
      )
    }
  }
}

class PersonMovedAddressEvent(
  override val aggregateId: UUID,
  override val values: PersonAddressDetailsValues,
) : Event(aggregateId, values) {
  override fun getType() = PERSON_MOVED_ADDRESS

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): PersonMovedAddressEvent {
      val personAddressDetailsValues = Klaxon()
        .fieldConverter(KlaxonUuid::class, uuidConverter)
        .fieldConverter(KlaxonAddressType::class, addressTypeConverter)
        .parse<PersonAddressDetailsValues>(eventEntity.values)!!

      return PersonMovedAddressEvent(
        aggregateId = eventEntity.aggregateId,
        values = personAddressDetailsValues,
      )
    }
  }
}

class AddressCreatedEvent(
  override val aggregateId: UUID,
  override val values: AddressDetailsValues
) : Event(aggregateId, values) {
  override fun getType() = CREATED_ADDRESS

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): AddressCreatedEvent {
      val addressDetailsValues = Klaxon().parse<AddressDetailsValues>(eventEntity.values)!!
      return AddressCreatedEvent(
        aggregateId = eventEntity.aggregateId,
        values = addressDetailsValues,
      )
    }
  }
}

class ChangedAddressEvent(
  override val aggregateId: UUID,
  override val values: AddressDetailsValues
) : Event(aggregateId, values) {
  override fun getType() = CHANGED_ADDRESS

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): ChangedAddressEvent {
      val addressDetailsValues = Klaxon().parse<AddressDetailsValues>(eventEntity.values)!!
      return ChangedAddressEvent(
        aggregateId = eventEntity.aggregateId,
        values = addressDetailsValues,
      )
    }
  }
}

class ApprovedAddressChangesEvent(
  override val aggregateId: UUID,
) : Event(aggregateId, emptyMap<String, String>()) {
  override fun getType() = CHANGES_APPROVED

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): ApprovedAddressChangesEvent {
      return ApprovedAddressChangesEvent(
        aggregateId = eventEntity.aggregateId,
      )
    }
  }
}
