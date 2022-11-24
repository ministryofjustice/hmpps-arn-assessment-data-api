package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import java.util.UUID

data class CreateAddressCommand(
  val aggregateId: UUID,
  val building: String,
  val postcode: String,
)

data class ChangeAddressCommand(
  val aggregateId: UUID,
  val building: String,
  val postcode: String,
)

data class ApproveAddressChangesCommand(
  val aggregateId: UUID,
)