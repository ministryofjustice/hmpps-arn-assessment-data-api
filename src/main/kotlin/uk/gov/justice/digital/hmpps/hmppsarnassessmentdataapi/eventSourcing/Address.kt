package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

class Address(private val eventStore: EventStore) {
  fun handle(command: Command) {
    val event = when (command) {
      is CreateAddress -> CreatedAddress(
        data = mapOf(
          "buildingName" to command.buildingName,
          "postcode" to command.postcode
        )
      )
      is UpdateAddress -> ChangedAddress(
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

