package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.PersonAddressRepository
import java.util.UUID

data class PersonAddressDto(
  val personId: UUID,
  val addressid: UUID,
  val addressType: AddressType,
  val building: String,
  val postcode: String,
)

@Service
class PersonQueryService(
  val personAddressRepository: PersonAddressRepository
) {
  fun getAddressesForPerson(personId: UUID): List<PersonAddressDto> {
    val addresses = personAddressRepository.findByPersonId(personId)

    return addresses.map { PersonAddressDto(
      it.personId,
      it.addressId,
      it.addressType,
      it.building,
      it.postcode,
    ) }
  }
}