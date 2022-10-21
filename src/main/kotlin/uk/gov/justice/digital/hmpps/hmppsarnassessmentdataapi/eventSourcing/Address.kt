package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_ADDRESS
import java.util.UUID

data class AddressState(
  var building: String? = "",
  var postcode: String? = "",
)

data class CreateAddress(
  val building: String,
  val postcode: String,
)

data class ChangeAddress(
  val building: String,
  val postcode: String,
)

data class CommandResponse(
  val aggregateId: UUID,
  val eventType: EventType,
  val values: Map<String, String>
) {
  companion object {
    fun from(event: EventEntity): CommandResponse {
      return CommandResponse(
        event.aggregateId,
        event.eventType,
        event.values,
      )
    }
  }
}

@Service
class Address(val eventStore: EventStore, val aggregateStore: AggregateStore) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun create(request: CreateAddress): CommandResponse {
    val uuid = aggregateStore.createAggregateRoot(ADDRESS)
    val event = EventEntity(
      aggregateId = uuid,
      eventType = CREATED_ADDRESS,
      values = mapOf(
        "building" to request.building,
        "postcode" to request.postcode,
      ),
    )

    eventStore.save(event)

    log.info("Created new address: $uuid")

    return CommandResponse.from(event)
  }

  fun change(uuid: UUID, request: ChangeAddress): CommandResponse {
    val addressExists = aggregateStore.checkAggregateRootExists(uuid)

    val event = EventEntity(
      aggregateId = uuid,
      eventType = CHANGED_ADDRESS,
      values = mapOf(
        "building" to request.building,
        "postcode" to request.postcode,
      ),
    )

    // can we make this smarter? like a diff?
    if (addressExists) {
      eventStore.save(event)

      log.info("Changed address: $uuid")
    }

    return CommandResponse.from(event)
  }

  fun markAsApproved(uuid: UUID): CommandResponse {
    val addressExists = aggregateStore.checkAggregateRootExists(uuid)
    val event =         EventEntity(
      aggregateId = uuid,
      eventType = CHANGES_APPROVED,
      values = emptyMap(),
    )

    if (addressExists) {
      eventStore.save(event)

      log.info("Approved changes for address: $uuid")
    }

    return CommandResponse.from(event)
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
      state.building = event.values["building"].orEmpty()
      state.postcode = event.values["postcode"].orEmpty()
    }
    CHANGED_ADDRESS -> {
      state.building = event.values["building"].orEmpty()
      state.postcode = event.values["postcode"].orEmpty()
    }
    CHANGES_APPROVED -> {} // skip events that don't build the aggregate
  }
  return state
}
