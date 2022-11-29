package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.CommandEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.CommandRepository
import java.util.UUID

class PendingCommandResponse(
  val commandId: UUID = UUID.randomUUID(),
  val commandType: CommandType,
  val commandValues: Map<String, String>,
) {
  companion object {
    fun from(commandEntity: CommandEntity): PendingCommandResponse {
      return PendingCommandResponse(
        commandId = commandEntity.uuid,
        commandType = commandEntity.type,
        commandValues = commandEntity.into(),
      )
    }
  }
}

@Service
class CommandStore(
  private val commandRepository: CommandRepository,
) {
  fun save(aggregateId: UUID, commandType: CommandType, values: Any): UUID {
    val entity = CommandEntity.from(aggregateId, commandType, values)
    commandRepository.save(entity)
    return entity.uuid
  }

  fun getCommand(commandId: UUID): CommandRequest? {
    return commandRepository.findByUuid(commandId)?.let { CommandRequest.from(it) }
  }

  fun getAllCommandsForAggregate(aggregateId: UUID): List<PendingCommandResponse> {
    return commandRepository.findByAggregateId(aggregateId).map { PendingCommandResponse.from(it) }
  }

  fun removeCommand(commandId: UUID) {
    commandRepository.removeByUuid(commandId)
  }
}
