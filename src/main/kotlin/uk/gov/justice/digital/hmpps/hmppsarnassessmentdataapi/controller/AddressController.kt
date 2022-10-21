package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AddressState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CreateAddress
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.ChangeAddress
import java.util.*

data class AddressCreated(
  val uuid: UUID,
)

@RestController
class AddressController(
  private val address: Address,
) {
  @RequestMapping(path = ["/address/create"], method = [RequestMethod.POST])
  fun createAddress(
    @RequestBody request: CreateAddress,
  ): AddressCreated {
    val uuid = address.create(request)

    return AddressCreated(uuid)
  }

  @RequestMapping(path = ["/address/{uuid}/update"], method = [RequestMethod.POST])
  fun updateAddress(
    @Parameter(required = true) @PathVariable uuid: UUID,
    @RequestBody request: ChangeAddress,
  ) {
    address.change(uuid, request)
  }

  @RequestMapping(path = ["/address/{uuid}/approve"], method = [RequestMethod.GET])
  fun approveUpdateAddress(
    @Parameter(required = true) @PathVariable uuid: UUID,
  ) {
    address.markAsApproved(uuid)
  }

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
