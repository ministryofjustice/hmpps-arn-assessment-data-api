package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonUuid
import java.util.UUID

data class CreateAddressCommand(
  val building: String,
  val postcode: String,
)

data class ChangeAddressCommand(
  @KlaxonUuid
  val aggregateId: UUID,
  val building: String,
  val postcode: String,
)

class ApproveAddressChangesCommand(
  @KlaxonUuid
  val commandUUID: UUID,
)
