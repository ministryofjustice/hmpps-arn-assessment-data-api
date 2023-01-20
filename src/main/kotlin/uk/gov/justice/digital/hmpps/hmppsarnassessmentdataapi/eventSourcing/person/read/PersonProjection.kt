package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read

import com.fasterxml.jackson.annotation.JsonView
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Projection
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Views
import java.time.LocalDate

data class PersonProjection(
  @field:JsonView(Views.Basic::class)
  val givenName: String? = null,

  @field:JsonView(Views.Basic::class)
  val familyName: String? = null,

  @field:JsonView(Views.Basic::class)
  val dateOfBirth: LocalDate? = null,
) : Projection()
