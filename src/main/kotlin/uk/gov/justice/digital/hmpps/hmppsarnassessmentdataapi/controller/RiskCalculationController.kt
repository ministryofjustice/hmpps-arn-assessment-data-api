package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller

import io.swagger.v3.oas.annotations.Parameter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation.read.RiskCalculationReadService
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.calculation.read.RiskCalculationState
import java.util.UUID

@RestController
class RiskCalculationController(
  private val riskCalculationReadService: RiskCalculationReadService,
) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @RequestMapping(path = ["/risk/{aggregateId}"], method = [RequestMethod.GET])
  fun getRiskCalculation(
    @Parameter(required = true) @PathVariable aggregateId: UUID,
  ): RiskCalculationState? {
    log.info("Entered RiskCalculationController getRiskCalculation")
    return RiskCalculationState.from(riskCalculationReadService.getCurrent(aggregateId)!!)
  }
}
