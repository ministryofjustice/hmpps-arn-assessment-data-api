package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATE_ADDRESS

interface Event {
  val type: AggregateType
  val eventType: EventType
  val data: Map<String, String>
}

enum class EventType {
  CREATE_ADDRESS,
  UPDATE_ADDRESS,
}

abstract class AddressEvent(override val type: AggregateType = AggregateType.ADDRESS) : Event

data class CreateAddressEvent(
  override val eventType: EventType = CREATE_ADDRESS,
  override val data: Map<String, String>,
) : AddressEvent()

data class UpdateAddressEvent(
  override val eventType: EventType = UPDATE_ADDRESS,
  override val data: Map<String, String>,
) : AddressEvent()
