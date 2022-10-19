package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.*

@Service
class EventStore(val eventRepository: EventRepository) {
  fun save(event: EventEntity) {
    eventRepository.save(event)
  }

  fun getAllEvents(uuid: UUID): List<EventEntity> {
    return eventRepository.findAllByAggregateId(uuid)
  }
}
