package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AggregateEntity
import java.util.UUID

@Repository
interface AggregateRepository : JpaRepository<AggregateEntity, Long> {
  fun existsByUuid(aggregateId: UUID): Boolean
}
