package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

class Address(val eventRepository: EventStore) {

  fun createAddressHandler(command: CreateAddress){
    val event = CreateAddressEvent(mapOf( "buildingName" to command.buildingName, "postcode" to command.postcode))
    eventRepository.save(event)
  }

}

