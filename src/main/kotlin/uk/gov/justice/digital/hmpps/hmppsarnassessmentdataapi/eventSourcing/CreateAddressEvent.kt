package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

abstract class AddressEvent(override val type:  AggregateType = AggregateType.ADDRESS): Event

data class CreateAddressEvent(override val data: Map<String, String>): AddressEvent()
