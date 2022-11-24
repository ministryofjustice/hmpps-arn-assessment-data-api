package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

data class AddressCreatedEvent(
  val building: String?,
  val postcode: String?,
)

data class AddressUpdatedEvent(
  val building: String?,
  val postcode: String?,
)
