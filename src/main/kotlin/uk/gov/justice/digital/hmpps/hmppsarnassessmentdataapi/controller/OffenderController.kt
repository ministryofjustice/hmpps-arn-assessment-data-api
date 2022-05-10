package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.OffenderDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service.OffenderService

@RestController
class OffenderController(
  private val offenderService: OffenderService
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @RequestMapping(path = ["/offender/crn/{crn}"], method = [RequestMethod.GET])
  fun getOffender(
    @Parameter(required = true) @PathVariable crn: String
  ): OffenderDto? {
    log.debug("Entered getOffender({})", crn)
    return offenderService.getOffenderByCrn(crn)
  }
}
