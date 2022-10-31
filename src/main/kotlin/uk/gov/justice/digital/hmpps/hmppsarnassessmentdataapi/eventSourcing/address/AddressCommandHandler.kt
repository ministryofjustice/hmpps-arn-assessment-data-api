package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Command
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressService

@Service
class AddressCommandHandler(
  val address: Address,
  val addressService: AddressService
) {
  fun createAddress(command: Command): List<CommandResponse> {
    val createdEvent = address.create(
      CreateAddress(
        building = command.values["building"].orEmpty(),
        postcode = command.values["postcode"].orEmpty(),
      )
    )

    val approvedEvent = address.markAsApproved(createdEvent.aggregateId)
    val current = address.buildCurrentState(createdEvent.aggregateId)
    addressService.saveCurrent(createdEvent.aggregateId, current)
    return listOf(createdEvent, approvedEvent)
  }

  fun changeAddress(command: Command): List<CommandResponse> {
    val changedEvent = address.change(
      command.aggregateId!!,
      ChangeAddress(
        building = command.values["building"].orEmpty(),
        postcode = command.values["postcode"].orEmpty(),
      )
    )

    val proposed = address.buildProposedState(changedEvent.aggregateId)
    addressService.saveProposed(changedEvent.aggregateId, proposed)
    return listOf(changedEvent)
  }

  fun approveChanges(command: Command): List<CommandResponse> {
    val approvedEvent = address.markAsApproved(command.aggregateId!!)

    val current = address.buildCurrentState(approvedEvent.aggregateId)
    addressService.deleteProposed(approvedEvent.aggregateId)
    addressService.saveCurrent(approvedEvent.aggregateId, current)

    return listOf(approvedEvent)
  }
}
