package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.RiskEntity
import java.util.UUID

@Repository
interface RiskRepository : JpaRepository<RiskEntity, Long> {
  fun getByAggregateId(aggregateId: UUID): RiskEntity?
}
