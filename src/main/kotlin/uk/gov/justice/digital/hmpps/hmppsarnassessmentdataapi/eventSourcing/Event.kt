package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import java.time.LocalDateTime
import java.util.UUID

class Event<T : Any>(
  val id: Long? = null,
  val uuid: UUID = UUID.randomUUID(),
  val createdOn: LocalDateTime = LocalDateTime.now(),
  val createdBy: String = "Unknown",
  val aggregateId: UUID,
  val eventType: EventType,
  val values: T,
)
