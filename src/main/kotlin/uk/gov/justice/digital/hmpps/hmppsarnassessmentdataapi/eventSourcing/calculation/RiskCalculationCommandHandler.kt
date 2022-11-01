package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Command
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse

@Service
class RiskCalculationCommandHandler(val riskCalculationService: RiskCalculationService) {
  fun createRiskCalculation(command: Command): List<CommandResponse> {
    val riskScore = riskCalculationService.createRiskCalculation(
      RiskValues(
        command.values["riskOne"]?.toDoubleOrNull(),
        command.values["riskTwo"]?.toDoubleOrNull()
      )
    )
    return listOf(CommandResponse.from(riskScore))
  }
}
