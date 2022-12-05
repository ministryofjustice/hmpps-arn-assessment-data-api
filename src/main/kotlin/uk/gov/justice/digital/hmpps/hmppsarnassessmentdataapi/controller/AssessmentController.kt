package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service.AssessmentService
import java.util.UUID

data class StartAssessmentRequest(
  val subjectUuid: UUID,
)

@RestController
class AssessmentController(
  private val assessmentService: AssessmentService,
) {
  @RequestMapping(path = ["/assessment"], method = [RequestMethod.POST])
  fun startAssessmentForSubject(
    @RequestBody() request: StartAssessmentRequest,
  ) = assessmentService.startAssessmentForSubject(request.subjectUuid)

  @RequestMapping(path = ["/assessment/{assessmentId}/offender"], method = [RequestMethod.GET])
  fun getAssessmentSubject(
    @Parameter(required = true) @PathVariable assessmentId: Long,
  ) = assessmentService.getOffenderForAssessment(assessmentId)

  @RequestMapping(path = ["/assessment/{assessmentId}/update"], method = [RequestMethod.GET])
  fun updateAssessment(
    @Parameter(required = true) @PathVariable assessmentId: Long,
  ) = assessmentService.update(assessmentId)
}
