package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

enum class EventType {
  CREATED_ADDRESS,
  CHANGED_ADDRESS,
  PROPOSED_ADDRESS_CHANGE,
  PERSON_MOVED_ADDRESS,
  CREATED_PERSON,
  PROPOSED_UPDATE_PERSON_DETAILS,
  UPDATED_PERSON_DETAILS,
}
