package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.RiskEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.RiskRepository

private val CURRENT_CALCULATION_VERSION = CalculatorVersion.ONE

enum class CalculatorVersion(val version: Double, val threshold: Double) {
  ONE(0.1, 5.0)
}

@Service
class RiskCalculationService(
  val riskCalculator: RiskCalculator,
  val aggregateStore: AggregateStore,
  val eventRepository: EventRepository,
  val riskRepository: RiskRepository
) {

  fun createRiskCalculation(riskValues: RiskValues): EventEntity {
    val aggregateId = aggregateStore.createAggregateRoot(AggregateType.RISK_CALCULATION)
    val score = riskCalculator.calculateScore(riskValues, CURRENT_CALCULATION_VERSION)

    val event = EventEntity(
      aggregateId = aggregateId,
      eventType = EventType.CREATED_RISK_CALCULATION,
      values = mapOf(
        "riskOne" to riskValues.riskOne.toString(),
        "riskTwo" to riskValues.riskTwo.toString(),
        "calculatorVersion" to CURRENT_CALCULATION_VERSION.toString(),
        "score" to score.score.toString(),
        "level" to score.level
      ),
    )
    eventRepository.save(event)
    riskRepository.save(
      RiskEntity(
        aggregateId = aggregateId,
        riskOne = riskValues.riskOne,
        riskTwo = riskValues.riskTwo,
        score = score.score,
        level = score.level,
        version = CURRENT_CALCULATION_VERSION.version.toString()
      )
    )
    return event
  }
}
