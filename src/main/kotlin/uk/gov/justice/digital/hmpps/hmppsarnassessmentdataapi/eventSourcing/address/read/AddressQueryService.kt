package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.UUID

@Service
class AddressQueryService(
  val eventRepository: EventRepository,
) {
  fun getAddress(addressId: UUID): AddressState {
    val addressEvents = eventRepository.findAllByAggregateId(addressId)

    return Address.aggregate(
      addressEvents
        .sortedBy { it.createdOn }
    )
  }
}
