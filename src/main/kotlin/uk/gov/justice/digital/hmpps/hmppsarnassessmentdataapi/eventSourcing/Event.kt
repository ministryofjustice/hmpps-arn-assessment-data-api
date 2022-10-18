package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

interface Event {
  val type: AggregateType
  val data: Map<String, String>
}