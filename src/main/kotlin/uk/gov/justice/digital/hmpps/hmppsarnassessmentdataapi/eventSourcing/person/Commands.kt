package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import java.time.LocalDate
import java.util.UUID

data class CreatePersonCommand(
  val givenName: String,
  val familyName: String,
  val dateOfBirth: LocalDate,
)

data class UpdatePersonCommand(
  val aggregateId: UUID,
  val givenName: String,
  val familyName: String,
  val dateOfBirth: LocalDate,
)

data class MovePersonAddressCommand(
  val aggregateId: UUID,
  val addressId: UUID,
  val addressType: AddressType,
)

data class ApprovePersonChangesCommand(
  val aggregateId: UUID,
)
