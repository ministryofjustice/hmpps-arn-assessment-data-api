package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.UUID

@Service
class AddressQueryService(
  val eventRepository: EventRepository,
) {
  fun getAddress(addressId: UUID) = eventRepository.findAllByAggregateId(addressId).let { events ->
    Address.createProjectionFrom(events.sortedBy { it.createdOn })
  }

  fun getChanges(personId: UUID, eventUuid: UUID) = eventRepository.findAllByAggregateId(personId).let { events ->
    Address.getChangesForEvent(eventUuid, events)
  }

  fun getEvents(personId: UUID) = eventRepository.findAllByAggregateId(personId)
    .sortedBy { it.createdOn }
    .map { EventDto.from(it) }
}
