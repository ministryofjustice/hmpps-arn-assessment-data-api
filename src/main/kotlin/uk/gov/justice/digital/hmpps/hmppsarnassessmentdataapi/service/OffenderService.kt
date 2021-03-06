package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.OffenderDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.OffenderEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.OffenderRepository

@Service
class OffenderService(
  private val offenderRepository: OffenderRepository
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun getOffenderByCrn(crn: String): OffenderDto? {
    log.debug("Entered getOffenderByCrn({})", crn)
    return OffenderDto.from(offenderRepository.findByCrn(crn))
  }

  fun updateOffender(offenderDto: OffenderDto) {
    log.debug("Entered updateOffender({})", offenderDto.crn)
    val offenderEntity = offenderRepository.findByCrn(offenderDto.crn)

    if (offenderEntity == null) {
      throw IllegalArgumentException("Offender not found for crn" + (offenderDto.crn))
    }

    OffenderEntity.update(offenderDto, offenderEntity)
    offenderRepository.save(offenderEntity)
  }
}
