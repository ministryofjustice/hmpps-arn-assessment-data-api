package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.AddressType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.Person
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonMovedAddressEvent
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.UUID

@Service
class PersonQueryService(
  val eventRepository: EventRepository,
) {
  fun getApprovedAddresses(personId: UUID): Map<AddressType, AddressState> {
    val personEvents = eventRepository.findAllByAggregateId(personId)
    val lastApprovedOn = personEvents.findLast { it.eventType == CHANGES_APPROVED }?.createdOn

    val personAddresses = personEvents
      .asSequence()
      .sortedBy { it.createdOn }
      .filter { it.createdOn < lastApprovedOn && it.eventType == PERSON_MOVED_ADDRESS }
      .map { PersonMovedAddressEvent.fromEventEntity(it) }
      .groupBy { it.values.addressType }
      .mapValues { (_, value) -> value.last().values.addressUUID }

    val addressEvents = eventRepository.findAllByAggregateIdIn(personAddresses.values.toList())
      .filter { it.createdOn < lastApprovedOn }

    val addresses = addressEvents
      .groupBy { it.aggregateId }
      .mapValues { (_, addressEvents) -> Address.aggregate(addressEvents) }

    return personAddresses.mapValues { (_, value) -> addresses[value]!! }
  }

  fun getPerson(personId: UUID): PersonState {
    val personEvents = eventRepository.findAllByAggregateId(personId)
    val lastApprovedOn = personEvents.findLast { it.eventType == CHANGES_APPROVED }?.createdOn

    return Person.aggregate(
      personEvents
        .sortedBy { it.createdOn }
        .filter { it.createdOn < lastApprovedOn }
    )
  }
}
