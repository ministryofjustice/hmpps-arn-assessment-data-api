package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AssessmentEntity
import java.time.LocalDateTime

@Repository
interface AssessmentRepository : JpaRepository<AssessmentEntity, Long> {

  fun findByOffenderCrnAndAssessmentTypeAndCompletedDate(crn: String, type: String, completedDate: LocalDateTime? = null): AssessmentEntity?
}
