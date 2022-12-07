package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressQueryService
import java.util.UUID

@RestController
class AddressController(
  private val addressQueryService: AddressQueryService,
) {
  @GetMapping("/address/{aggregateId}")
  fun getAddress(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ) = addressQueryService.getAddress(aggregateId)
}
