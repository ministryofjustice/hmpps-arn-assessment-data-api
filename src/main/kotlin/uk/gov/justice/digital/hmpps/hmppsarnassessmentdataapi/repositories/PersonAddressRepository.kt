package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.PersonAddressEntity
import java.util.UUID

@Repository
interface PersonAddressRepository : JpaRepository<PersonAddressEntity, Long> {
  fun findByPersonId(personId: UUID): List<PersonAddressEntity>
}
