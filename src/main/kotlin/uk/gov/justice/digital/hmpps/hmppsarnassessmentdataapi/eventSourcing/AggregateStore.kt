package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AggregateEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.AggregateRepository
import java.util.UUID

@Service
class AggregateStore(private val aggregateRepository: AggregateRepository) {
  fun checkAggregateRootExists(uuid: UUID): Boolean {
    return aggregateRepository.existsByUuid(uuid)
  }

  fun createAggregateRoot(aggregateType: AggregateType): UUID {
    val newAggregate = AggregateEntity(aggregateType = aggregateType)
    aggregateRepository.save(newAggregate)
    return newAggregate.uuid
  }
}
