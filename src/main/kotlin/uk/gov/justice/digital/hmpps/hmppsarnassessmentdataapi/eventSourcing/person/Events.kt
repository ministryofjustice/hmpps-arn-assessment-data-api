package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Event
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATED_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.JsonEventValues
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonAddressType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonDate
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonUuid
import java.time.LocalDate
import java.util.UUID

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

class PersonCreatedEvent(
  override val aggregateId: UUID,
  override val values: PersonDetailsValues,
) : Event(aggregateId, values) {
  override fun getType() = CREATED_PERSON

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): PersonCreatedEvent {
      val personDetailsValues = JsonEventValues.deserialize<PersonDetailsValues>(eventEntity.values)!!

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
      val personDetailsValues = JsonEventValues.deserialize<PersonDetailsValues>(eventEntity.values)!!

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
      val personAddressDetailsValues = JsonEventValues.deserialize<PersonAddressDetailsValues>(eventEntity.values)!!

      return PersonMovedAddressEvent(
        aggregateId = eventEntity.aggregateId,
        values = personAddressDetailsValues,
      )
    }
  }
}
