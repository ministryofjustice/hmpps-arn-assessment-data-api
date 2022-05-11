package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.OffenderEntity

@Repository
interface OffenderRepository : JpaRepository<OffenderEntity, Long> {

  fun findByCrn(crn: String): OffenderEntity?
}
