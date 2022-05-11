package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto

data class UpdateAssessmentDto(
  val id: Long,
  var supportNeeds: MutableList<SupportNeedDto> = mutableListOf()
  )
