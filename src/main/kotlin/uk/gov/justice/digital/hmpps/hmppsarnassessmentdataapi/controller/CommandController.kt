package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandHandler
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandRequest
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse

@RestController
class CommandController(
  private val commandHandler: CommandHandler,
) {
  @RequestMapping(path = ["/command"], method = [RequestMethod.POST])
  fun executeCommands(
    @RequestBody commands: List<CommandRequest>,
  ): List<CommandResponse> {
    return commandHandler.handleAll(commands)
  }
}
