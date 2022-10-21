package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import java.util.UUID

@Repository
interface EventRepository : JpaRepository<EventEntity, Long> {
  fun findAllByAggregateIdOrderByCreatedOnAsc(uuid: UUID): List<EventEntity>
}
