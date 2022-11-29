package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_ADDRESS_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.MOVE_PERSONS_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.PROPOSE_ADDRESS_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.PROPOSE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.UPDATE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PROPOSED_ADDRESS_CHANGE
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PROPOSED_UPDATE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATED_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.AddressCommandHandler
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.ApproveAddressChangesCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.ChangeAddressCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.CreateAddressCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.ApprovePersonChangesCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.CreatePersonCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.MovePersonAddressCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonCommandHandler
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.UpdatePersonCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.JsonEventValues

enum class CommandType {
  CREATE_ADDRESS,
  PROPOSE_ADDRESS_CHANGES,
  APPROVE_ADDRESS_CHANGES,
  CREATE_PERSON,
  UPDATE_PERSON_DETAILS,
  PROPOSE_PERSON_DETAILS,
  APPROVE_PERSON_DETAILS,
  MOVE_PERSONS_ADDRESS,
}

data class CommandRequest(
  val type: CommandType,
  val values: Map<String, String>,
) {
  inline fun <reified T> into(): T {
    return JsonEventValues.deserialize(JsonEventValues.serialize(values))!!
  }
}

@Service
class CommandHandler(
  val addressCommandHandler: AddressCommandHandler,
  val personCommandHandler: PersonCommandHandler,
  val commandStore: CommandStore,
) {
  fun handleAll(commands: List<CommandRequest>): List<CommandResponse> {
    return commands.map { handle(it) }.flatten()
  }

  fun handle(command: CommandRequest): List<CommandResponse> {
    return when (command.type) {
      CREATE_ADDRESS -> addressCommandHandler.handle(command.into<CreateAddressCommand>())
      PROPOSE_ADDRESS_CHANGES -> {
        val aggregateId = command.into<ChangeAddressCommand>().aggregateId
        val commandUUID = commandStore.save(aggregateId, command)
        return listOf(CommandResponse(aggregateId, PROPOSED_ADDRESS_CHANGE, mapOf("commandId" to commandUUID.toString())))
      }
      APPROVE_ADDRESS_CHANGES -> {
        val approvalCommand = command.into<ApproveAddressChangesCommand>()
        val pendingCommandRequest = commandStore.getCommand(approvalCommand.commandUUID)!!
        val pendingCommand = pendingCommandRequest.into<ChangeAddressCommand>()
        addressCommandHandler.handle(pendingCommand)

        return listOf(CommandResponse(pendingCommand.aggregateId, CHANGED_ADDRESS, pendingCommandRequest.values))
      }
      CREATE_PERSON -> personCommandHandler.handle(command.into<CreatePersonCommand>())
      UPDATE_PERSON_DETAILS -> personCommandHandler.handle(command.into<UpdatePersonCommand>())
      MOVE_PERSONS_ADDRESS -> personCommandHandler.handle(command.into<MovePersonAddressCommand>())
      PROPOSE_PERSON_DETAILS -> {
        val aggregateId = command.into<UpdatePersonCommand>().aggregateId
        val commandUUID = commandStore.save(aggregateId, command)
        return listOf(CommandResponse(aggregateId, PROPOSED_UPDATE_PERSON_DETAILS, mapOf("commandId" to commandUUID.toString())))
      }
      APPROVE_PERSON_DETAILS -> {
        val approvalCommand = command.into<ApprovePersonChangesCommand>()
        val pendingCommandRequest = commandStore.getCommand(approvalCommand.commandUUID)!!
        val pendingCommand = pendingCommandRequest.into<UpdatePersonCommand>()
        personCommandHandler.handle(pendingCommandRequest.into<UpdatePersonCommand>())

        return listOf(CommandResponse(pendingCommand.aggregateId, UPDATED_PERSON_DETAILS, pendingCommandRequest.values))
      }
    }
  }
}
