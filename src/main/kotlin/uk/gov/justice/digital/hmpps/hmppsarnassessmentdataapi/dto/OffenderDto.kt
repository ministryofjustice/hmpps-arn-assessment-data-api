package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.Gender
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.OffenderEntity
import java.time.LocalDateTime

data class OffenderDto(
  val id: Long? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val crn: String,
  val gender: Gender? = null,
  val createdDate: LocalDateTime? = null
) {
  companion object {
    fun from(entity: OffenderEntity?): OffenderDto? {
      return if (entity == null) null
      else OffenderDto(
        id = entity.id,
        firstName = entity.firstName,
        lastName = entity.lastName,
        crn = entity.crn,
        gender = entity.gender,
        createdDate = entity.createdDate
      )
    }
  }
}
