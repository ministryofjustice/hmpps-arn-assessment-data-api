package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.ApprovedEventRepository
import java.util.UUID

@Service
class PersonQueryService(
  val approvedEventRepository: ApprovedEventRepository,
) {
  fun getAddressesForPerson(personId: UUID): Map<AddressType, AddressState> {
    val personEvents = approvedEventRepository.findAllByAggregateIdOrderByCreatedOnAsc(personId)
    val personAddresses = personEvents
      .sortedBy { it.createdOn }
      .filter { it.eventType == PERSON_MOVED_ADDRESS }
      .groupBy { it.values["addressType"].orEmpty() }
      .filterKeys { it.isNotBlank() }
      .mapKeys { (key, _) -> AddressType.valueOf(key) }
      .mapValues { (_, value) -> value.last().values["addressUuid"].orEmpty() }
      .filterValues { it.isNotBlank() }
      .mapValues { (_, value) -> UUID.fromString(value) }

    val addressEvents = approvedEventRepository.findAllByAggregateIdIn(personAddresses.values.toList())

    val addresses = addressEvents
      .groupBy { it.aggregateId }
      .mapValues { (_, value) -> Address.aggregate(value) }

    return personAddresses.mapValues { (_, value) -> addresses[value]!! }
  }

  fun getPerson(personId: UUID): PersonState {
    val personEvents = approvedEventRepository.findAllByAggregateIdOrderByCreatedOnAsc(personId)

    return Person.aggregate(personEvents)
  }
}
