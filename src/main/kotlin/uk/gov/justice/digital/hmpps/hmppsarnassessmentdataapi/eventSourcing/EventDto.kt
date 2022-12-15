package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import java.time.LocalDateTime
import java.util.UUID

data class EventDto(
  val uuid: UUID = UUID.randomUUID(),
  val createdOn: LocalDateTime = LocalDateTime.now(),
  val createdBy: String = "Unknown",
  val aggregateId: UUID,
  val eventType: EventType,
) {
  companion object {
    fun from(eventEntity: EventEntity) = with(eventEntity) {
      EventDto(uuid, createdOn, createdBy, aggregateId, eventType)
    }
  }
}
