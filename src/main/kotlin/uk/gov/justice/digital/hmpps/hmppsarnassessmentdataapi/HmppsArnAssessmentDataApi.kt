package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class HmppsArnAssessmentDataApi

fun main(args: Array<String>) {
  runApplication<HmppsArnAssessmentDataApi>(*args)
}
