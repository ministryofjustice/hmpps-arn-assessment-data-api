package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read.PersonState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service.AssessmentService
import java.util.UUID

data class StartAssessmentRequest(
  val subjectUuid: UUID,
)

@RestController
class AssessmentController(
  private val assessmentService: AssessmentService,
) {
  @PostMapping("/assessment")
  fun startAssessmentForSubject(
    @RequestBody() request: StartAssessmentRequest,
  ) = assessmentService.startAssessmentForSubject(request.subjectUuid)

  @GetMapping("/assessment/{assessmentId}/offender")
  fun getAssessmentSubject(
    @Parameter(required = true) @PathVariable assessmentId: Long,
  ): PersonState? {
    return assessmentService.getOffenderForAssessment(assessmentId)
  }

  @GetMapping("/assessment/{assessmentId}/update")
  fun updateAssessment(
    @Parameter(required = true) @PathVariable assessmentId: Long,
  ) = assessmentService.update(assessmentId)
}
