package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressState
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository

@Service
class Address(val eventRepository: EventRepository, val aggregateStore: AggregateStore) {
  fun handle(command: CreateAddressCommand): CommandResponse {
    val aggregateId = aggregateStore.createAggregateRoot(ADDRESS)
    val event = EventEntity.from(
      aggregateId = aggregateId,
      eventType = CREATED_ADDRESS,
      values = AddressCreatedEvent(
        building = command.building,
        postcode = command.postcode,
      )
    )

    eventRepository.save(event)

    log.info("Created new address: $aggregateId")

    return CommandResponse.from(event)
  }

  fun handle(command: ChangeAddressCommand): CommandResponse {
    val addressExists = aggregateStore.checkAggregateRootExists(command.aggregateId)

    val event = EventEntity.from(
      aggregateId = command.aggregateId,
      eventType = CHANGED_ADDRESS,
      values = AddressCreatedEvent(
        building = command.building,
        postcode = command.postcode,
      )
    )

    // can we make this smarter? like a diff?
    if (addressExists) {
      eventRepository.save(event)
      log.info("Changed address: $command.aggregateId")
    }

    return CommandResponse.from(event)
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun aggregate(events: List<EventEntity>) = events
      .sortedBy { it.createdOn }
      .fold(AddressState()) { state: AddressState, event: EventEntity -> applyEvent(state, event) }

    private fun apply(state: AddressState, event: AddressCreatedEvent) = AddressState(
      building = event.building ?: state.building,
      postcode = event.postcode ?: state.postcode,
    )

    @JvmName("apply1")
    private fun apply(state: AddressState, event: AddressUpdatedEvent) = AddressState(
      building = event.building ?: state.building,
      postcode = event.postcode ?: state.postcode,
    )

    private fun applyEvent(state: AddressState, eventEntity: EventEntity): AddressState {
      return when (eventEntity.eventType) {
        CREATED_ADDRESS -> apply(state, eventEntity.into<AddressCreatedEvent>())
        CHANGED_ADDRESS -> apply(state, eventEntity.into<AddressUpdatedEvent>())
        else -> state // skip events that don't build the aggregate
      }
    }
  }
}
