package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["uk.gov.justice.digital"], exclude = [FlywayAutoConfiguration::class])
class HmppsArnAssessmentDataApi

fun main(args: Array<String>) {
  runApplication<HmppsArnAssessmentDataApi>(*args)
}
