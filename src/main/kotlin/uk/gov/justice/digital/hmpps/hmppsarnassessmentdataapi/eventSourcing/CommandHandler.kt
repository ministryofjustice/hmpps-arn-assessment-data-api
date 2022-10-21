package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_ADDRESS_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CHANGE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_ADDRESS
import java.util.UUID

enum class CommandType {
  CREATE_ADDRESS,
  CHANGE_ADDRESS,
  APPROVE_ADDRESS_CHANGES,
}

data class Command(
  val type: CommandType,
  val aggregateId: UUID? = null,
  val values: Map<String, String>,
)

@Service
class CommandHandler(
  val address: Address,
  val addressStore: AddressStore,
) {
  fun handleAll(commands: List<Command>): List<List<CommandResponse>> {
    return commands.map { handle(it) }
  }

  fun handle(command: Command): List<CommandResponse> {
    when (command.type) {
      CREATE_ADDRESS -> {
        val createdEvent = address.create(
          CreateAddress(
            building = command.values["building"].orEmpty(),
            postcode = command.values["postcode"].orEmpty(),
          )
        )

        val approvedEvent = address.markAsApproved(createdEvent.aggregateId)

        val current = address.buildCurrentState(createdEvent.aggregateId)
        addressStore.saveCurrent(createdEvent.aggregateId, current)

        return listOf(createdEvent, approvedEvent)
      }
      CHANGE_ADDRESS -> {
        val changedEvent = address.change(
          command.aggregateId!!,
          ChangeAddress(
            building = command.values["building"].orEmpty(),
            postcode = command.values["postcode"].orEmpty(),
          )
        )

        val proposed = address.buildProposedState(changedEvent.aggregateId)
        addressStore.saveProposed(changedEvent.aggregateId, proposed)

        return listOf(changedEvent)
      }
      APPROVE_ADDRESS_CHANGES -> {
        val approvedEvent = address.markAsApproved(command.aggregateId!!)

        val current = address.buildCurrentState(approvedEvent.aggregateId)
        addressStore.deleteProposed(approvedEvent.aggregateId)
        addressStore.saveCurrent(approvedEvent.aggregateId, current)

        return listOf(approvedEvent)
      }
    }
  }
}
