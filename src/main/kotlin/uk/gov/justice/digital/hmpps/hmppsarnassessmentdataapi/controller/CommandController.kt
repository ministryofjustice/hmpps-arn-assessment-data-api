package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandHandler
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandRequest
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.PendingCommandResponse
import java.util.UUID

@RestController
class CommandController(
  private val commandHandler: CommandHandler,
  private val commandStore: CommandStore,
) {
  @RequestMapping(path = ["/command"], method = [RequestMethod.POST])
  fun executeCommands(
    @RequestBody commands: List<CommandRequest>,
  ): List<CommandResponse> {
    return commandHandler.handleAll(commands)
  }

  @RequestMapping(path = ["/command/{aggregateId}/pending"], method = [RequestMethod.GET])
  fun getPendingCommands(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): List<PendingCommandResponse> {
    return commandStore.getAllCommandsForAggregate(aggregateId)
  }
}
