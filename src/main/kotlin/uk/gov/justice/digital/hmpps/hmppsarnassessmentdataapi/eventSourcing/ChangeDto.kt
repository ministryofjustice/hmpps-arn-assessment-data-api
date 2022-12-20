package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

data class ChangeDto(
  val field: String = "",
  val from: String = "",
  val to: String = "",
)
