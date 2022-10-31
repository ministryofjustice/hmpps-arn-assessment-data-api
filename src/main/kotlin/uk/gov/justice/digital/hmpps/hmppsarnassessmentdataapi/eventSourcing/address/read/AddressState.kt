package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AddressEntity

data class AddressState(
  var building: String = "",
  var postcode: String = "",
) {
  companion object {
    fun from(addressEntity: AddressEntity): AddressState {
      return AddressState(
              building = addressEntity.building,
              postcode = addressEntity.postcode,
      )
    }
  }
}