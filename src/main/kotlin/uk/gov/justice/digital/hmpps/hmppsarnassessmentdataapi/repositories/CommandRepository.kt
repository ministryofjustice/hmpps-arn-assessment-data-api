package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.CommandEntity
import java.util.UUID

@Repository
interface CommandRepository : JpaRepository<CommandEntity, Long> {
  fun findByAggregateId(aggregateId: UUID): List<CommandEntity>
  fun findByUuid(commandId: UUID): CommandEntity?
  fun removeByUuid(commandId: UUID)
}
