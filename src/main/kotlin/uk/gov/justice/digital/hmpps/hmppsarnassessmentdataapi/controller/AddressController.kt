package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AddressState
import java.util.UUID


@RestController
class AddressController(
  private val address: Address,
) {
  @RequestMapping(path = ["/address/{uuid}"], method = [RequestMethod.GET])
  fun getAddress(
    @Parameter(required = true) @PathVariable uuid: UUID,
  ): AddressState {
    return address.find(uuid)
  }

  @RequestMapping(path = ["/address/{uuid}/pending"], method = [RequestMethod.GET])
  fun getPendingChangesForAddress(
    @Parameter(required = true) @PathVariable uuid: UUID,
  ): AddressState {
    return address.getUnapprovedChanges(uuid)
  }
}
