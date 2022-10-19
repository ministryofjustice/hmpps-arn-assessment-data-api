package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import java.time.Instant

interface Event {
  val aggregateType: AggregateType
  val eventType: EventType
  val createdOn: Instant
  val data: Map<String, String>
}

enum class EventType {
  CREATED_ADDRESS,
  CHANGED_ADDRESS,
  CHANGES_APPROVED,
}

abstract class BaseEvent(final override val createdOn: Instant = Instant.now()) : Event

abstract class AddressEvent(
  final override val aggregateType: AggregateType = AggregateType.ADDRESS,
) : BaseEvent()

data class AddressChangeApproved(
  override val eventType: EventType = EventType.CHANGES_APPROVED,
  override val data: Map<String, String>,
) : AddressEvent()

data class CreatedAddress(
  override val eventType: EventType = EventType.CREATED_ADDRESS,
  override val data: Map<String, String>,
) : AddressEvent()

data class ChangedAddress(
  override val eventType: EventType = EventType.CHANGED_ADDRESS,
  override val data: Map<String, String>,
) : AddressEvent()
