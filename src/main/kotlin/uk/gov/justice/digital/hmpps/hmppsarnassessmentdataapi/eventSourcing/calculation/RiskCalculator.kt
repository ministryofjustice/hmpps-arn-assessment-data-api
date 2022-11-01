package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation

import org.springframework.stereotype.Service

@Service
class RiskCalculator {
  fun calculateScore(riskValues: RiskValues, version: CalculatorVersion): RiskScore {
    when (version) {
      CalculatorVersion.ONE -> {
        val score = (riskValues.riskOne ?: 0.0) + (riskValues.riskTwo ?: 0.0)
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
