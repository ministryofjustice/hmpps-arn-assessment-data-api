package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.AddressEntity
import java.util.*

@Repository
interface AddressRepository : JpaRepository<AddressEntity, Long> {
  fun existsByUuid(uuid: UUID): Boolean
}
