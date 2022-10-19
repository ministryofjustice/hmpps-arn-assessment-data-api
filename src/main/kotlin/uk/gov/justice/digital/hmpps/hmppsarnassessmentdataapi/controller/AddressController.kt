package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AddressState
import java.util.*

data class CreateAddress(
  val building: String,
  val postcode: String,
)

data class UpdateAddress(
  val building: String? = null,
  val postcode: String? = null,
)

@RestController
class AddressController(
  private val address: Address,
) {
  @RequestMapping(path = ["/address/create"], method = [RequestMethod.POST])
  fun createAddress(
    @RequestBody request: CreateAddress,
  ) {
    address.create(request.building, request.postcode)
  }

  @RequestMapping(path = ["/address/{uuid}/update"], method = [RequestMethod.POST])
  fun updateAddress(
    @Parameter(required = true) @PathVariable uuid: UUID,
    @RequestBody request: UpdateAddress,
  ) {
    address.update(uuid, request.building, request.postcode)
  }

  @RequestMapping(path = ["/address/{uuid}/approve"], method = [RequestMethod.GET])
  fun approveUpdateAddress(
    @Parameter(required = true) @PathVariable uuid: UUID,
  ) {
    address.approveChanges(uuid)
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
