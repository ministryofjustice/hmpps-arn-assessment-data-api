package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import java.util.UUID

class CommandEntity(
  val commandId: UUID = UUID.randomUUID(),
  val aggregateId: UUID,
  val commandType: CommandType,
  val commandValues: Map<String, String>,
) {
  fun intoCommandRequest(): CommandRequest {
    return CommandRequest(
      type = commandType,
      values = commandValues
    )
  }

  companion object {
    fun from(aggregateUUID: UUID, commandRequest: CommandRequest): CommandEntity {
      return CommandEntity(
        commandType = commandRequest.type,
        aggregateId = aggregateUUID,
        commandValues = commandRequest.values,
      )
    }
  }
}

class PendingCommandResponse(
  val commandId: UUID = UUID.randomUUID(),
  val commandType: CommandType,
  val commandValues: Map<String, String>,
) {
  companion object {
    fun from(commandEntity: CommandEntity): PendingCommandResponse {
      return PendingCommandResponse(
        commandId = commandEntity.commandId,
        commandType = commandEntity.commandType,
        commandValues = commandEntity.commandValues,
      )
    }
  }
}

@Service
class CommandStore(
  private val commandRepository: MutableList<CommandEntity> = mutableListOf()
) {
  fun save(aggregateId: UUID, commandRequest: CommandRequest): UUID {
    val entity = CommandEntity.from(aggregateId, commandRequest)
    commandRepository.add(entity)
    return entity.commandId
  }

  fun getCommand(commandUUID: UUID): CommandRequest? {
    return commandRepository.find { it.commandId == commandUUID }?.intoCommandRequest()
  }

  fun getAllCommandsForAggregate(aggregateId: UUID): List<PendingCommandResponse> {
    return commandRepository.filter { it.aggregateId == aggregateId }.map { PendingCommandResponse.from(it) }
  }

  fun removeCommand(commandUUID: UUID) {
    commandRepository.removeAll { it.commandId == commandUUID }
  }
}
