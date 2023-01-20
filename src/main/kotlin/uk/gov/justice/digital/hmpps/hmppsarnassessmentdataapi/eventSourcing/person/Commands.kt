package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonDate
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonUuid
import java.time.LocalDate
import java.util.UUID

data class CreateNewPersonCommand(
  val givenName: String,
  val familyName: String,
  @KlaxonDate
  val dateOfBirth: LocalDate,
)

data class ProposeUpdatePersonDetailsCommand(
  @KlaxonUuid
  val aggregateId: UUID,
  val givenName: String,
  val familyName: String,
  @KlaxonDate
  val dateOfBirth: LocalDate,
)

class ApproveUpdatePersonDetailsCommand(
  @KlaxonUuid
  val commandUUID: UUID,
)

data class UpdatePersonDetailsCommand(
  @KlaxonUuid
  val aggregateId: UUID,
  val givenName: String,
  val familyName: String,
  @KlaxonDate
  val dateOfBirth: LocalDate,
)

data class MovePersonAddressCommand(
  @KlaxonUuid
  val aggregateId: UUID,
  val addressId: UUID,
  val addressType: AddressType,
)
