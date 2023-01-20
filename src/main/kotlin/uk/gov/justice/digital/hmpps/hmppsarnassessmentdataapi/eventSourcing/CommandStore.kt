package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.CommandEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.CommandRepository
import java.util.UUID
import javax.transaction.Transactional

class PendingCommandResponse(
  val commandId: UUID = UUID.randomUUID(),
  val commandType: CommandType,
  val commandValues: Map<String, String>,
) {
  companion object {
    fun from(commandEntity: CommandEntity) = with(commandEntity) {
      PendingCommandResponse(
        commandId = uuid,
        commandType = type,
        commandValues = into(),
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

  fun getCommand(commandId: UUID) = commandRepository.findByUuid(commandId)?.let { CommandRequest.from(it) }

  fun getAllCommandsForAggregate(aggregateId: UUID) =
    commandRepository.findByAggregateId(aggregateId).map { PendingCommandResponse.from(it) }

  @Transactional
  fun removeCommand(commandId: UUID) = commandRepository.removeByUuid(commandId)
}
