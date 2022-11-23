package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Event
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.JsonEventValues
import java.util.UUID

data class AddressDetailsValues(
  val building: String?,
  val postcode: String?,
)

class AddressCreatedEvent(
  override val aggregateId: UUID,
  override val values: AddressDetailsValues
) : Event(aggregateId, values) {
  override fun getType() = CREATED_ADDRESS

  companion object {
    fun fromEventEntity(eventEntity: EventEntity): AddressCreatedEvent {
      val addressDetailsValues = JsonEventValues.deserialize<AddressDetailsValues>(eventEntity.values)!!
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
      val addressDetailsValues = JsonEventValues.deserialize<AddressDetailsValues>(eventEntity.values)!!
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
