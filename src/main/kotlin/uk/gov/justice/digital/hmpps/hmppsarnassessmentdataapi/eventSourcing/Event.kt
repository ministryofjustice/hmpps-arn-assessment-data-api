package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.JsonEventValues
import java.util.UUID

abstract class Event(
  open val aggregateId: UUID,
  open val values: Any,
) {
  abstract fun getType(): EventType

  fun toEventEntity(): EventEntity {
    val serializedValues = JsonEventValues.serialize(values)

    return EventEntity(
      aggregateId = aggregateId,
      eventType = getType(),
      values = serializedValues,
    )
  }
}
