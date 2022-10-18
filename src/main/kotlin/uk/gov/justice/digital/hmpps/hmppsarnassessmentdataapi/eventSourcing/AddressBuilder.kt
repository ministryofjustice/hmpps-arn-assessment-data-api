package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATE_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATE_ADDRESS

class AddressState(
  var buildingName: String? = null,
  var postcode: String? = null
)

class AddressBuilder(private val eventStore: EventStore) {
  fun load(): AddressState {
    val events = eventStore.getAll()

    return events.fold(AddressState()) { acc, event ->
      when (event.eventType) {
        CREATE_ADDRESS -> {
          acc.buildingName = event.data["buildingName"]
          acc.postcode = event.data["postcode"]
        }
        UPDATE_ADDRESS -> {
          event.data["buildingName"].let { acc.buildingName = event.data["buildingName"] }
          event.data["postcode"].let { acc.postcode = event.data["postcode"] }
        }
        else -> {}
      }
      acc
    }
  }
}
