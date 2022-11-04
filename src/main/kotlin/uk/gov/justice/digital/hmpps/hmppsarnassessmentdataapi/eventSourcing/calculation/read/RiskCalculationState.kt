package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation.read

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.RiskEntity
import java.util.UUID

data class RiskCalculationState(
  val uuid: UUID,
  val aggregateId: UUID,
  var riskOne: Double?,
  var riskTwo: Double?,
  var score: Double,
  val level: String,
  val version: String
) {
  companion object {
    fun from(riskEntity: RiskEntity): RiskCalculationState {
      return RiskCalculationState(
        uuid = riskEntity.uuid,
        aggregateId = riskEntity.aggregateId,
        riskOne = riskEntity.riskOne,
        riskTwo = riskEntity.riskTwo,
        score = riskEntity.score,
        level = riskEntity.level,
        version = riskEntity.version
      )
    }
  }
}
