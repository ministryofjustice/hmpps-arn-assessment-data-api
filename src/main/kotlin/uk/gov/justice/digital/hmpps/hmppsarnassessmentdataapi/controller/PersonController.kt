package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read.PersonQueryService
import java.util.UUID

@RestController
class PersonController(
  private val personQueryService: PersonQueryService,
) {
  @GetMapping("/person/{aggregateId}/addresses")
  fun getAddressesForPerson(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ) = personQueryService.getApprovedAddresses(aggregateId)

  @GetMapping("/person/{aggregateId}")
  fun getPerson(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ) = personQueryService.getPerson(aggregateId)
}
