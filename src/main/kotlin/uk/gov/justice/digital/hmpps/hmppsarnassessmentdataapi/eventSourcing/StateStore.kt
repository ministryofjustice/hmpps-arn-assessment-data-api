package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AddressEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.AddressRepository
import java.util.*

@Service
class StateStore(private val addressRepository: AddressRepository) {
  fun find(uuid: UUID): AddressEntity? {
    return addressRepository.findByUuid(uuid)
  }

  fun createNewAddress(): UUID {
    val address = AddressEntity(uuid = UUID.randomUUID())
    addressRepository.save(address)
    return address.uuid
  }
}
