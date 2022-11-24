package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonAddressType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonDate
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.KlaxonUuid
import java.time.LocalDate
import java.util.UUID

data class CreatedPersonEvent(
  val givenName: String? = null,
  val familyName: String? = null,
  @KlaxonDate
  val dateOfBirth: LocalDate? = null,
)

data class PersonUpdatedDetailsEvent(
  val givenName: String? = null,
  val familyName: String? = null,
  @KlaxonDate
  val dateOfBirth: LocalDate? = null,
)

data class PersonMovedAddressEvent(
  @KlaxonUuid
  val addressUUID: UUID,
  @KlaxonAddressType
  val addressType: AddressType,
)
