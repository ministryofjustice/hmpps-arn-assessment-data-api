package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressQueryService
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressStateStore

@Service
class AddressCommandHandler(
  val address: Address,
  val addressStateStore: AddressStateStore,
  val addressQueryService: AddressQueryService,
) {
  fun handle(command: CreateAddressCommand): List<CommandResponse> {
    val createdEvent = address.handle(command)
    val approvedEvent = address.handle(ApproveAddressChangesCommand(createdEvent.aggregateId))

    val current = addressQueryService.getApprovedAddress(createdEvent.aggregateId)
    addressStateStore.saveCurrent(createdEvent.aggregateId, current)

    return listOf(createdEvent, approvedEvent)
  }

  fun handle(command: ChangeAddressCommand): List<CommandResponse> {
    val changedEvent = address.handle(command)

    val proposed = addressQueryService.getProposedChanges(changedEvent.aggregateId)
    addressStateStore.saveProposed(changedEvent.aggregateId, proposed)

    return listOf(changedEvent)
  }

  fun handle(command: ApproveAddressChangesCommand): List<CommandResponse> {
    val approvedEvent = address.handle(command)

    val current = addressQueryService.getApprovedAddress(approvedEvent.aggregateId)
    addressStateStore.deleteProposed(approvedEvent.aggregateId)
    addressStateStore.saveCurrent(approvedEvent.aggregateId, current)

    return listOf(approvedEvent)
  }
}
