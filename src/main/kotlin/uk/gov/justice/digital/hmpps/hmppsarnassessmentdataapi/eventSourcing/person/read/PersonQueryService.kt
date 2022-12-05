package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.AddressType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.Person
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonMovedAddressEvent
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.UUID

@Service
class PersonQueryService(
  val eventRepository: EventRepository,
) {
  fun getApprovedAddresses(personId: UUID): Map<AddressType, AddressState> {
    val personEvents = eventRepository.findAllByAggregateId(personId)

    val personAddresses = personEvents
      .asSequence()
      .sortedBy { it.createdOn }
      .map { it.into<PersonMovedAddressEvent>() }
      .groupBy { it.addressType }
      .mapValues { (_, value) -> value.last().addressUUID }

    val addressEvents = eventRepository.findAllByAggregateIdIn(personAddresses.values.toList())

    val addresses = addressEvents
      .groupBy { it.aggregateId }
      .mapValues { (_, addressEvents) -> Address.aggregateFrom(addressEvents) }

    return personAddresses.mapValues { (_, value) -> addresses[value]!! }
  }

  fun getPerson(personId: UUID) = eventRepository.findAllByAggregateId(personId).let { events ->
    Person.aggregateFrom(events.sortedBy { it.createdOn })
  }
}
