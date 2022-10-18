package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

interface Event

abstract class AddressEvent(val type:  AggregateType = AggregateType.ADDRESS): Event