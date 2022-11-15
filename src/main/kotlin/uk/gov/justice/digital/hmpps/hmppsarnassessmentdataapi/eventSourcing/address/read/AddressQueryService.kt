package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.UUID

@Service
class AddressQueryService(
  val eventRepository: EventRepository,
) {
  fun getApprovedAddress(addressId: UUID): AddressState {
    val addressEvents = eventRepository.findAllByAggregateId(addressId)
    val lastApprovedOn = addressEvents.findLast { it.eventType == CHANGES_APPROVED }?.createdOn

    return Address.aggregate(
      addressEvents
        .sortedBy { it.createdOn }
        .filter { it.createdOn < lastApprovedOn }
    )
  }

  fun getProposedChanges(addressId: UUID): AddressState {
    val addressEvents = eventRepository.findAllByAggregateId(addressId)
    val lastApprovedOn = addressEvents.findLast { it.eventType == CHANGES_APPROVED }?.createdOn

    return Address.aggregate(
      addressEvents
        .sortedBy { it.createdOn }
        .filter { it.createdOn > lastApprovedOn }
    )
  }
}