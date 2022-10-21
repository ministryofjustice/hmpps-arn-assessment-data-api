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
) {
  fun handleAll(commands: List<Command>): List<CommandResponse> {
    return commands.map { handle(it) }
  }

  fun handle(command: Command): CommandResponse {
    return when (command.type) {
      CREATE_ADDRESS -> address.create(
          CreateAddress(
            building = command.values["building"].orEmpty(),
            postcode = command.values["postcode"].orEmpty(),
          ))
      CHANGE_ADDRESS -> address.change(
          command.aggregateId!!,
          ChangeAddress(
            building = command.values["building"].orEmpty(),
            postcode = command.values["postcode"].orEmpty(),
          ))
      APPROVE_ADDRESS_CHANGES -> address.markAsApproved(command.aggregateId!!)
    }
  }
}