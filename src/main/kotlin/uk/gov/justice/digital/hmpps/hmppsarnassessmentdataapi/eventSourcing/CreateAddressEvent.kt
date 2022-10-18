package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing


data class CreateAddressEvent(val data: Map<String, String>): AddressEvent()
