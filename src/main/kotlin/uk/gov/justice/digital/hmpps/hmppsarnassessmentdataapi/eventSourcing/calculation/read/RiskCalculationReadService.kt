package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation.read

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.RiskEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.RiskRepository
import java.util.UUID

@Service
class RiskCalculationReadService(val riskRepository: RiskRepository) {
  fun getCurrent(aggregateId: UUID): RiskEntity? {
    return riskRepository.getByAggregateId(aggregateId)
  }
}
