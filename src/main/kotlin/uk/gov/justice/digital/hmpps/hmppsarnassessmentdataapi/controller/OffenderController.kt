package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.OffenderDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service.OffenderService

@RestController
class OffenderController(
  private val offenderService: OffenderService
) {
  @RequestMapping(path = ["/offender/crn/{crn}"], method = [RequestMethod.GET])
  fun getOffender(
    @Parameter(required = true) @PathVariable crn: String
  ) = offenderService.getOffenderByCrn(crn)

  @RequestMapping(path = ["/offender"], method = [RequestMethod.PUT])
  fun updateOffender(
    @RequestBody offenderDto: OffenderDto
  ): ResponseEntity<HttpStatus> {
    offenderService.updateOffender(offenderDto)
    return ResponseEntity(HttpStatus.ACCEPTED)
  }
}
