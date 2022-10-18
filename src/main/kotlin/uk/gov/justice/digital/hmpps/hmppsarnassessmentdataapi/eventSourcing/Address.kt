package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

class Address(val eventStore: EventStore) {
  fun handle(command: Command) {
    val event = when (command) {
      is CreateAddress -> CreateAddressEvent(
        data = mapOf(
          "buildingName" to command.buildingName,
          "postcode" to command.postcode
        )
      )
      is UpdateAddress -> UpdateAddressEvent(
        data = mapOf(
          "buildingName" to command.buildingName,
          "postcode" to command.postcode
        )
      )
      else -> null
    }

    event.let {
      eventStore.save(event!!)
    }
  }
}

