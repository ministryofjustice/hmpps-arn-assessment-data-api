package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import java.util.UUID

@Repository
interface ApprovedEventRepository : JpaRepository<EventEntity, Long> {
  fun findAllByAggregateIdOrderByCreatedOnAsc(aggregateId: UUID): List<EventEntity>
  fun findAllByAggregateIdIn(aggregateIds: List<UUID>): List<EventEntity>
}
