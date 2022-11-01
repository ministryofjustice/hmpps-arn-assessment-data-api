package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository


private val CURRENT_CALCULATION_VERSION = CalculatorVersion.ONE

enum class CalculatorVersion(val version: Double, val threshold: Double) {
  ONE(0.1, 5.0)
}

  @Service
class CalculationEventService(val riskCalculationService: RiskCalculationService, val aggregateStore: AggregateStore, val eventRepository: EventRepository) {

  fun calculateRiskScore(riskCalculation: RiskCalculation): CommandResponse {
    val aggregateId = aggregateStore.createAggregateRoot(AggregateType.RISK_CALCULATION)
    val score = riskCalculationService.calculateScore(riskCalculation, CURRENT_CALCULATION_VERSION)

    val event = EventEntity(
      aggregateId = aggregateId,
      eventType = EventType.CREATED_RISK_CALCULATION,
      values = mapOf(
        "riskOne" to riskCalculation.riskOne.toString(),
        "riskTwo" to riskCalculation.riskTwo.toString(),
        "calculatorVersion" to CURRENT_CALCULATION_VERSION.toString(),
        "score" to score.score.toString(),
        "level" to score.level
      ),
    )
    eventRepository.save(event)
    return CommandResponse.from(event)
  }
}
