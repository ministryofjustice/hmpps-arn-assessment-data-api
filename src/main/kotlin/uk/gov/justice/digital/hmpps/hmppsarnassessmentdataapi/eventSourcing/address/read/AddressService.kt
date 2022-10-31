package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AddressEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.StateType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.StateType.CURRENT
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.StateType.PROPOSED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.AddressRepository
import java.util.UUID

@Service
class AddressService(val addressRepository: AddressRepository) {
  fun saveCurrent(aggregateId: UUID, addressState: AddressState) {
    save(aggregateId, addressState, CURRENT)
  }

  fun saveProposed(aggregateId: UUID, addressState: AddressState) {
    save(aggregateId, addressState, PROPOSED)
  }

  fun save(aggregateId: UUID, addressState: AddressState, state: StateType) {
    val address = addressRepository.findByAggregateIdAndState(aggregateId, state)

    if (address != null) {
      address.building = addressState.building
      address.postcode = addressState.postcode
      addressRepository.save(address)
    } else {
      addressRepository.save(
        AddressEntity(
          aggregateId = aggregateId,
          building = addressState.building,
          postcode = addressState.postcode,
          state = state,
        )
      )
    }
  }

  fun getCurrent(aggregateId: UUID): AddressEntity? {
    return addressRepository.findByAggregateIdAndState(aggregateId, CURRENT)
  }

  fun getProposed(aggregateId: UUID): AddressEntity? {
    return addressRepository.findByAggregateIdAndState(aggregateId, PROPOSED)
  }

  fun deleteProposed(aggregateId: UUID) {
    addressRepository.deleteByAggregateIdAndState(aggregateId, PROPOSED)
  }
}
