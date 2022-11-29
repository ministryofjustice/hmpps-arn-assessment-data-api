package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

enum class EventType {
  CREATED_ADDRESS,
  ADDRESS_DETAILS_UPDATED,
  CREATED_PERSON,
  PERSON_DETAILS_UPDATED,
  PERSON_MOVED_ADDRESS,
  APPROVED_CHANGES,
  PROPOSED_CHANGES,
}
