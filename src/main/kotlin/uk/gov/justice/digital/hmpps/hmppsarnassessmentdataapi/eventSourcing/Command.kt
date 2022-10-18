package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import java.time.Instant

// basically a DTO
abstract class Command(createdAt: Instant = Instant.now())

class CreateAddress (
  val buildingName: String,
  val postcode: String
): Command()