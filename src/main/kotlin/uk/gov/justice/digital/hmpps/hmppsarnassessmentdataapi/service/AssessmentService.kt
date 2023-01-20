package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AssessmentEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.AggregateRepository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.AssessmentRepository
import java.time.LocalDateTime
import java.util.UUID

@Service
class AssessmentService(
  private val assessmentRepository: AssessmentRepository,
  private val aggregateRepository: AggregateRepository,
) {
  fun getOffenderForAssessment(id: Long) = assessmentRepository.findById(id).get().getOffender()

  fun startAssessmentForSubject(subjectUuid: UUID): Long {
    return aggregateRepository.findByUuid(subjectUuid)?.let {
      val assessment = AssessmentEntity(
        assessmentType = "TEST",
        version = "TEST",
        offender = it,
      )

      return assessmentRepository.save(assessment).id ?: throw Exception("Failed to save assessment")
    } ?: throw Exception("Failed to start assessment")
  }

  fun update(id: Long) {
    val assessment = assessmentRepository.findById(id).get()

    assessment.updatedOnDate = LocalDateTime.now()

    assessmentRepository.save(assessment)
  }
}
