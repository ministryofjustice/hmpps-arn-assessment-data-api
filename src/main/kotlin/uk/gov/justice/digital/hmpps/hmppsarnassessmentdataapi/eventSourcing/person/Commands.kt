package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonDate
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonUuid
import java.time.LocalDate
import java.util.UUID

data class CreatePersonCommand(
  val givenName: String,
  val familyName: String,
  @KlaxonDate
  val dateOfBirth: LocalDate,
)

data class UpdatePersonCommand(
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

class ApprovePersonChangesCommand(
  @KlaxonUuid
  val aggregateId: UUID,
)
