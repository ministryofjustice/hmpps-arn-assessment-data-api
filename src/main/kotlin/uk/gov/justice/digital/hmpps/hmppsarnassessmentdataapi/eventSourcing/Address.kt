package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_ADDRESS
import java.util.*

data class AddressState(
  var buildingName: String? = null,
  var postcode: String? = null
)

@Service
class Address(val eventStore: EventStore, val stateStore: StateStore) {
  fun create(buildingName: String, postcode: String) {
    val rootUuid = stateStore.createNewAddress()
    eventStore.save(
      EventEntity(
        aggregateId = rootUuid,
        aggregateType = ADDRESS,
        eventType = CREATED_ADDRESS,
        values = mapOf(
          "buildingName" to buildingName,
          "postcode" to postcode
        ),
      )
    )
  }

  fun update(uuid: UUID, buildingName: String?, postcode: String?) {
    val root = stateStore.find(uuid)
    root.let {
      eventStore.save(
        EventEntity(
          aggregateId = root!!.uuid,
          aggregateType = ADDRESS,
          eventType = CHANGED_ADDRESS,
          values = mapOf(
            "buildingName" to buildingName,
            "postcode" to postcode
          ),
        )
      )
    }
  }

  fun approveChanges(uuid: UUID) {
    val root = stateStore.find(uuid)

    if (root != null) {
      eventStore.save(
        EventEntity(
          aggregateId = root.uuid,
          aggregateType = ADDRESS,
          eventType = CHANGES_APPROVED,
          values = emptyMap(),
        )
      )
    }
  }

  fun find(uuid: UUID): AddressState {
    val events = eventStore.getAllEvents(uuid)

    val lastApproval = events.indexOfLast { it.eventType == CHANGES_APPROVED }

    return events.slice(0..lastApproval).fold(AddressState()) { state, event -> applyEvent(state, event) }
  }

  fun getUnapprovedChanges(uuid: UUID): AddressState {
    val events = eventStore.getAllEvents(uuid)

    val lastApproval = events.indexOfLast { it.eventType == CHANGES_APPROVED }

    return events.slice(lastApproval..events.lastIndex)
      .fold(AddressState()) { state, event -> applyEvent(state, event) }
  }
}

private fun applyEvent(state: AddressState, event: EventEntity): AddressState {
  when (event.eventType) {
    CREATED_ADDRESS -> {
      state.buildingName = event.values["buildingName"]
      state.postcode = event.values["postcode"]
    }
    CHANGED_ADDRESS -> {
      if (!event.values["buildingName"].isNullOrEmpty()) {
        state.buildingName = event.values["buildingName"]
      }
      if (!event.values["postcode"].isNullOrEmpty()) {
        state.postcode = event.values["postcode"]
      }
    }
    CHANGES_APPROVED -> {} // skip events that don't build the aggregate
  }
  return state
}
