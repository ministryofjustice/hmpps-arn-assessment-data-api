package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonUuid
import java.util.UUID

data class CreateNewAddressCommand(
  val building: String,
  val postcode: String,
)

data class ProposeUpdateAddressDetailsCommand(
  @KlaxonUuid
  val aggregateId: UUID,
  val building: String,
  val postcode: String,
)

data class UpdateAddressDetailsCommand(
  @KlaxonUuid
  val aggregateId: UUID,
  val building: String,
  val postcode: String,
)

class ApproveUpdateAddressDetailsCommand(
  @KlaxonUuid
  val commandUUID: UUID,
)
