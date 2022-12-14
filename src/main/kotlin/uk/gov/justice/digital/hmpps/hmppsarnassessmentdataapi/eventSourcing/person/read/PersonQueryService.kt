package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressProjection
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.AddressType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.Person
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonMovedAddressEvent
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.UUID

@Service
class PersonQueryService(
  val eventRepository: EventRepository,
) {
  fun getApprovedAddresses(personId: UUID): Map<AddressType, AddressProjection> {
    val personEvents = eventRepository.findAllByAggregateId(personId)

    val personAddresses = personEvents
      .asSequence()
      .filter { it.eventType == PERSON_MOVED_ADDRESS }
      .sortedBy { it.createdOn }
      .map { it.into<PersonMovedAddressEvent>() }
      .groupBy { it.addressType }
      .mapValues { it.value.last().addressUUID }

    val addressEvents = eventRepository.findAllByAggregateIdIn(personAddresses.values.toList())

    val addresses = addressEvents
      .groupBy { it.aggregateId }
      .mapValues { Address.createProjectionFrom(it.value) }

    return personAddresses.mapValues { addresses[it.value]!! }
  }

  fun getPerson(personId: UUID) = eventRepository.findAllByAggregateId(personId).let { events ->
    Person.createProjectionFrom(events.sortedBy { it.createdOn })
  }
}
