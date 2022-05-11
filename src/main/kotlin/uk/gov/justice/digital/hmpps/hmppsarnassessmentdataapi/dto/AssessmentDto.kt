package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AssessmentEntity
import java.time.LocalDateTime

data class AssessmentDto(
  val assessmentType: String,
  val version: String,
  val offender: OffenderDto?,
  var supportNeeds: List<SupportNeedDto> = emptyList(),
  val createdDate: LocalDateTime? = LocalDateTime.now(),
  val completedDate: LocalDateTime? = null,
  ) {

  companion object {

    fun from(assessmentEntity: AssessmentEntity?): AssessmentDto? {
      return if (assessmentEntity == null) null
      else (
        AssessmentDto(
          assessmentType = assessmentEntity.assessmentType,
          version = assessmentEntity.version,
          offender = OffenderDto.from(assessmentEntity.offender),
          supportNeeds = SupportNeedDto.from(assessmentEntity.supportNeeds),
          createdDate = assessmentEntity.createdDate,
          completedDate = assessmentEntity.completedDate
        )
        )
    }
  }
}
