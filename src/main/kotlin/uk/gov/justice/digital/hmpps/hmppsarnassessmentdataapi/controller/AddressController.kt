package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressService
import java.util.UUID

@RestController
class AddressController(
  private val addressService: AddressService,
) {
  @RequestMapping(path = ["/address/{aggregateId}"], method = [RequestMethod.GET])
  fun getAddress(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): AddressState? {
    return AddressState.from(addressService.getCurrent(aggregateId)!!)
  }

  @RequestMapping(path = ["/address/{aggregateId}/pending"], method = [RequestMethod.GET])
  fun getPendingChangesForAddress(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): AddressState {
    return AddressState.from(addressService.getProposed(aggregateId)!!)
  }
}
