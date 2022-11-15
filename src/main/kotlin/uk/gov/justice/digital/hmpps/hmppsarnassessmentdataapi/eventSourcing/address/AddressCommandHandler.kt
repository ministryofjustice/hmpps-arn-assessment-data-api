package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Command
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressQueryService
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressStateStore

@Service
class AddressCommandHandler(
  val address: Address,
  val addressStateStore: AddressStateStore,
  val addressQueryService: AddressQueryService,
) {
  fun createAddress(command: Command): List<CommandResponse> {
    val createdEvent = address.handle(
      CreateAddressCommand(
        aggregateId = command.aggregateId!!,
        building = command.values["building"].orEmpty(),
        postcode = command.values["postcode"].orEmpty(),
      )
    )

    val approvedEvent = address.handle(ApproveAddressChangesCommand(createdEvent.aggregateId))
    val current = addressQueryService.getApprovedAddress(createdEvent.aggregateId)
    addressStateStore.saveCurrent(createdEvent.aggregateId, current)
    return listOf(createdEvent, approvedEvent)
  }

  fun changeAddress(command: Command): List<CommandResponse> {
    val changedEvent = address.handle(
      ChangeAddressCommand(
        aggregateId = command.aggregateId!!,
        building = command.values["building"].orEmpty(),
        postcode = command.values["postcode"].orEmpty(),
      )
    )

    val proposed = addressQueryService.getProposedChanges(changedEvent.aggregateId)
    addressStateStore.saveProposed(changedEvent.aggregateId, proposed)
    return listOf(changedEvent)
  }

  fun approveChanges(command: Command): List<CommandResponse> {
    val approvedEvent = address.handle(ApproveAddressChangesCommand(command.aggregateId!!))

    val current = addressQueryService.getApprovedAddress(approvedEvent.aggregateId)
    addressStateStore.deleteProposed(approvedEvent.aggregateId)
    addressStateStore.saveCurrent(approvedEvent.aggregateId, current)

    return listOf(approvedEvent)
  }
}
