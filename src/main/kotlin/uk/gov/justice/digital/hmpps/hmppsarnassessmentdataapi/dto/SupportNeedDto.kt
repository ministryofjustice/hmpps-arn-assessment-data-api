package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.SupportNeedEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.SupportNeedsType

data class SupportNeedDto(
  val type: SupportNeedsType? = null,
  val details: String? = null
  ) {

  companion object {
    fun from(supportNeeds: MutableList<SupportNeedEntity>): List<SupportNeedDto> {
      return supportNeeds.map { from(it) }.toList()
    }

    private fun from(supportNeedEntity: SupportNeedEntity): SupportNeedDto {
      return SupportNeedDto(
        type = supportNeedEntity.type,
        details = supportNeedEntity.details
      )
    }
  }
}
