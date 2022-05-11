package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.AssessmentDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.UpdateAssessmentDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AssessmentEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service.AssessmentService

@RestController
class AssessmentController(
  private val assessmentService: AssessmentService
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @RequestMapping(path = ["/offender/crn/{crn}/assessment/type/{type}"], method = [RequestMethod.GET])
  fun getInProgressAssessment(
    @Parameter(required = true) @PathVariable crn: String,
    @Parameter(required = true) @PathVariable type: String
  ): AssessmentDto? {
    log.debug("Entered getInProgressAssessment({})", crn)
    return assessmentService.getInProgressAssessment(crn, type)
  }

  @RequestMapping(path = ["/offender/crn/{crn}/assessment/type/{type}/version/{version}"], method = [RequestMethod.POST])
  fun createNewAssessment(
    @Parameter(required = true) @PathVariable crn: String,
    @Parameter(required = true) @PathVariable type: String,
    @Parameter(required = true) @PathVariable version: String
  ): AssessmentDto? {
    log.debug("Entered createNewAssessment($crn, $type)")
    return assessmentService.createNewAssessment(crn, type, version)
  }

  @RequestMapping(path = ["/assessment"], method = [RequestMethod.PUT])
  fun updateAssessment(
    @RequestBody updateAssessmentDto: UpdateAssessmentDto
  ) : AssessmentEntity {
    log.debug("Entered updateAssessment({})", updateAssessmentDto)
    return assessmentService.updateAssessment(updateAssessmentDto)

  }
}
