package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

class AddressBuilder(private val eventStore: EventStore) {

  fun buildAddressState(): AddressState {
    val events = eventStore.getAll()
    val accumulator: AddressState = AddressState()
    events.forEach {
      when (it) {
        is CreateAddressEvent ->{
          accumulator.buildingName = it.data["buildingName"]
          accumulator.postcode = it.data["postcode"] }
        else -> {}
      }
    }
    return accumulator

  }
}

class AddressState(
  var buildingName: String? = null,
  var postcode: String? = null
)