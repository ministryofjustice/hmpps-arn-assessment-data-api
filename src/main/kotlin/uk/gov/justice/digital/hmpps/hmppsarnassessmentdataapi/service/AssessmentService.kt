package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.AssessmentDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.UpdateAssessmentDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AssessmentEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.AssessmentRepository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.OffenderRepository
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@Service
class AssessmentService(
  private val assessmentRepository: AssessmentRepository,
  private val offenderRepository: OffenderRepository
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun getInProgressAssessment(crn: String, type: String): AssessmentDto? {
    log.debug("Entered getAssessment($crn, $type)")
    return AssessmentDto.from(
      assessmentRepository.findByOffenderCrnAndAssessmentTypeAndCompletedDate(crn, type)
        ?: throw EntityNotFoundException("No in progress assessment found with crn: $crn, type: $type")
    )
  }

  fun createNewAssessment(crn: String, type: String, version: String): AssessmentDto? {
    log.debug("Entered createNewAssessment($crn, $type, $version)")

    val offenderEntity = offenderRepository.findByCrn(crn) ?: throw EntityNotFoundException("No offender with crn: $crn")
    val assessmentEntity = AssessmentEntity(
      assessmentType = type,
      version = version,
      offender = offenderEntity,
      createdDate = LocalDateTime.now()
    )
    assessmentRepository.save(assessmentEntity)
    return AssessmentDto.from(assessmentEntity)
  }

  fun updateAssessment(updateAssessmentDto: UpdateAssessmentDto): AssessmentEntity {
    log.debug("Entered updateAssessment(${updateAssessmentDto.id})")
    val assessmentEntity = assessmentRepository.findByIdOrNull(updateAssessmentDto.id) ?: throw EntityNotFoundException()
    assessmentEntity.update(updateAssessmentDto)
    assessmentRepository.save(assessmentEntity)
    return assessmentEntity
  }
}
