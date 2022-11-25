package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_ADDRESS_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_PERSON_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CHANGE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.MOVE_PERSONS_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.UPDATE_PERSON_DETAILS
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
  CHANGE_ADDRESS,
  APPROVE_ADDRESS_CHANGES,
  CREATE_PERSON,
  UPDATE_PERSON_DETAILS,
  MOVE_PERSONS_ADDRESS,
  APPROVE_PERSON_CHANGES,
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
) {
  fun handleAll(commands: List<CommandRequest>): List<CommandResponse> {
    return commands.map { handle(it) }.flatten()
  }

  fun handle(command: CommandRequest): List<CommandResponse> {
    return when (command.type) {
      CREATE_ADDRESS -> addressCommandHandler.handle(command.into<CreateAddressCommand>())
      CHANGE_ADDRESS -> addressCommandHandler.handle(command.into<ChangeAddressCommand>())
      APPROVE_ADDRESS_CHANGES -> addressCommandHandler.handle(command.into<ApproveAddressChangesCommand>())
      CREATE_PERSON -> personCommandHandler.handle(command.into<CreatePersonCommand>())
      UPDATE_PERSON_DETAILS -> personCommandHandler.handle(command.into<UpdatePersonCommand>())
      MOVE_PERSONS_ADDRESS -> personCommandHandler.handle(command.into<MovePersonAddressCommand>())
      APPROVE_PERSON_CHANGES -> personCommandHandler.handle(command.into<ApprovePersonChangesCommand>())
    }
  }
}
