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
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.PersonCommandHandler
import java.util.UUID

enum class CommandType {
  CREATE_ADDRESS,
  CHANGE_ADDRESS,
  APPROVE_ADDRESS_CHANGES,
  CREATE_PERSON,
  UPDATE_PERSON_DETAILS,
  MOVE_PERSONS_ADDRESS,
  APPROVE_PERSON_CHANGES,
}

data class Command(
  val type: CommandType,
  val aggregateId: UUID? = null,
  val values: Map<String, String>,
)

@Service
class CommandHandler(
  val addressCommandHandler: AddressCommandHandler,
  val personCommandHandler: PersonCommandHandler,
) {
  fun handleAll(commands: List<Command>): List<List<CommandResponse>> {
    return commands.map { handle(it) }
  }

  fun handle(command: Command): List<CommandResponse> {
    return when (command.type) {
      CREATE_ADDRESS -> addressCommandHandler.createAddress(command)
      CHANGE_ADDRESS -> addressCommandHandler.changeAddress(command)
      APPROVE_ADDRESS_CHANGES -> addressCommandHandler.approveChanges(command)
      CREATE_PERSON -> personCommandHandler.createPerson(command)
      UPDATE_PERSON_DETAILS -> personCommandHandler.updatePersonDetails(command)
      MOVE_PERSONS_ADDRESS -> personCommandHandler.moveAddress(command)
      APPROVE_PERSON_CHANGES -> personCommandHandler.approveChanges(command)
    }
  }
}
