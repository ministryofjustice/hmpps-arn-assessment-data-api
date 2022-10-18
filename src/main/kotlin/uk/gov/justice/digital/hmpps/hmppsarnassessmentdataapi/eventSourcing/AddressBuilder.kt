package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

class AddressState(
  var buildingName: String? = null,
  var postcode: String? = null
)

class AddressBuilder(private val eventStore: EventStore) {
  fun load(): AddressState {
    val events = eventStore.getAll()

    return events.fold(AddressState()) { acc, event ->
        when (event) {
          is CreateAddressEvent -> {
            acc.buildingName = event.data["buildingName"]
            acc.postcode = event.data["postcode"]
          }
          else -> {}
        }
        acc
    }
  }
}
