package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.ChangeDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressProjection
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressQueryService
import java.util.UUID

@RestController
class AddressController(
  private val addressQueryService: AddressQueryService,
) {
  @GetMapping("/address/{aggregateId}")
  fun getAddress(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): AddressProjection {
    return addressQueryService.getAddress(aggregateId)
  }

  @GetMapping("/address/{aggregateId}/events")
  fun getEvents(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): List<EventDto> {
    return addressQueryService.getEvents(aggregateId)
  }

  @GetMapping("/address/{aggregateId}/events/{eventId}")
  fun getEvents(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
    @Parameter(required = true) @PathVariable eventId: UUID,
  ): Map<String, ChangeDto> {
    return addressQueryService.getChanges(aggregateId, eventId)
  }
}
