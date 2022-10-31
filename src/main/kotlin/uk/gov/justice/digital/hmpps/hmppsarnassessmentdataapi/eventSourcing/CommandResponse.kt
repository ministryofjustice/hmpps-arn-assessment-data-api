package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import java.util.UUID

data class CommandResponse(
  val aggregateId: UUID,
  val eventType: EventType,
  val values: Map<String, String>
) {
  companion object {
    fun from(event: EventEntity): CommandResponse {
      return CommandResponse(
        event.aggregateId,
        event.eventType,
        event.values,
      )
    }
  }
}
