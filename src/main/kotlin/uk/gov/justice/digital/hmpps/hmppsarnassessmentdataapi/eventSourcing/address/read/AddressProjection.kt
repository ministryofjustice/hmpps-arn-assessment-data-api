package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Projection

data class AddressProjection(
  var building: String = "",
  var postcode: String = "",
) : Projection()
