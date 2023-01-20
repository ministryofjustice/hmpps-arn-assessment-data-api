package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

class AddressCreatedEvent

data class AddressDetailsUpdatedEvent(
  val building: String?,
  val postcode: String?,
)

data class ApprovedAddressChangesEvent(
  val approvedBy: String = "Unknown",
)
