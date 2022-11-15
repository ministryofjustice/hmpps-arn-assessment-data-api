package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.AddressType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonQueryService
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonState
import java.util.UUID

@RestController
class PersonController(
  private val personQueryService: PersonQueryService,
) {
  @RequestMapping(path = ["/person/{aggregateId}/addresses"], method = [RequestMethod.GET])
  fun getAddressesForPerson(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): Map<AddressType, AddressState> {
    return personQueryService.getApprovedAddresses(aggregateId)
  }

  @RequestMapping(path = ["/person/{aggregateId}"], method = [RequestMethod.GET])
  fun getPerson(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): PersonState {
    return personQueryService.getApprovedPersonDetails(aggregateId)
  }
}
