package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Command
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse

@Service
class CalculationCommandHandler(val calculationEventService: CalculationEventService) {
  fun createRiskCalculation(command: Command): List<CommandResponse> {
    val riskScore = calculationEventService.calculateRiskScore(
      RiskCalculation(
        command.values["riskOne"]?.toDoubleOrNull(),
        command.values["riskTwo"]?.toDoubleOrNull()
      )
    )
    return listOf(riskScore)
  }
}
