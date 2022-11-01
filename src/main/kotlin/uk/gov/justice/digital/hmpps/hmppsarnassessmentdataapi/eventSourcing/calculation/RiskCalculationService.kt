package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation

import org.springframework.stereotype.Service

@Service
class RiskCalculationService {
  fun calculateScore(riskCalculation: RiskCalculation, version: CalculatorVersion): RiskScore {
    when (version) {
      CalculatorVersion.ONE -> {
        val score = (riskCalculation.riskOne ?: 0.0) + (riskCalculation.riskTwo ?: 0.0)
        val level = if (score > version.threshold) "HIGH" else "LOW"
        return RiskScore(
          score = score,
          level = level
        )
      }
    }
  }
}

class RiskScore(
  val score: Double,
  val level: String
)
