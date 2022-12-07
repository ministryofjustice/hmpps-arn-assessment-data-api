package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandHandler
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandRequest
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandStore
import java.util.UUID

@RestController
class CommandController(
  private val commandHandler: CommandHandler,
  private val commandStore: CommandStore,
) {
  @PostMapping("/command")
  fun executeCommands(
    @RequestBody commands: List<CommandRequest>,
  ) = commandHandler.handleAll(commands)

  @DeleteMapping("/command/{commandId}/pending")
  fun removeCommand(
    @Parameter(required = true) @PathVariable commandId: UUID,
  ) = commandStore.removeCommand(commandId)

  @GetMapping("/command/{aggregateId}/pending")
  fun getPendingCommands(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ) = commandStore.getAllCommandsForAggregate(aggregateId)
}
