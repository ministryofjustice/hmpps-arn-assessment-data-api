package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_ADDRESS_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CHANGE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.AddressCommandHandler
import java.util.UUID

enum class CommandType {
  CREATE_ADDRESS,
  CHANGE_ADDRESS,
  APPROVE_ADDRESS_CHANGES
}

data class Command(
  val type: CommandType,
  val aggregateId: UUID? = null,
  val values: Map<String, String>,
)

@Service
class CommandHandler(
  val addressCommandHandler: AddressCommandHandler
) {
  fun handleAll(commands: List<Command>): List<List<CommandResponse>> {
    return commands.map { handle(it) }
  }

  fun handle(command: Command): List<CommandResponse> {
    return when (command.type) {
      CREATE_ADDRESS -> {
        addressCommandHandler.createAddress(command)
      }
      CHANGE_ADDRESS -> {
        addressCommandHandler.changeAddress(command)
      }
      APPROVE_ADDRESS_CHANGES -> {
        addressCommandHandler.approveChanges(command)
      }
    }
  }
}
