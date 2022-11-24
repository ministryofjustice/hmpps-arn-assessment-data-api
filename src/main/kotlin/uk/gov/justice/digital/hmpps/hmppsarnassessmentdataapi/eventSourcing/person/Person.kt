package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Event
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATED_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.time.LocalDate

enum class AddressType {
  PRIMARY_RESIDENCE,
  KNOWN_ADDRESS,
}

data class PersonState(
  val givenName: String? = null,
  val familyName: String? = null,
  val dateOfBirth: LocalDate? = null,
)

@Service
class Person(
  val address: Address,
  val eventRepository: EventRepository,
  val aggregateStore: AggregateStore,
) {
  fun handle(command: CreatePersonCommand): CommandResponse {
    val aggregateId = aggregateStore.createAggregateRoot(PERSON)
    val event = EventEntity.from(
      aggregateId = aggregateId,
      eventType = CREATED_PERSON,
      values = CreatedPersonEvent(
        givenName = command.givenName,
        familyName = command.familyName,
        dateOfBirth = command.dateOfBirth,
      )
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun handle(command: UpdatePersonCommand): CommandResponse {
    val event = EventEntity.from(
      aggregateId = command.aggregateId,
      eventType = UPDATED_PERSON_DETAILS,
      values = PersonUpdatedDetailsEvent(
        givenName = command.givenName,
        familyName = command.familyName,
        dateOfBirth = command.dateOfBirth,
      )
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun handle(command: ApprovePersonChangesCommand): CommandResponse {
    val event = EventEntity.from(
      aggregateId = command.aggregateId,
      eventType = CHANGES_APPROVED,
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun handle(command: MovePersonAddressCommand): CommandResponse {
    val event = EventEntity.from(
      aggregateId = command.aggregateId,
      eventType = PERSON_MOVED_ADDRESS,
      values = PersonMovedAddressEvent(
        addressUUID = command.addressId,
        addressType = command.addressType,
      )
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  companion object {
    fun aggregate(events: List<EventEntity>) = events
      .sortedBy { it.createdOn }
      .fold(PersonState()) { state: PersonState, event: EventEntity -> applyEvent(state, event) }

    private fun apply(state: PersonState, event: CreatedPersonEvent) = PersonState(
      givenName = event.givenName ?: state.givenName,
      familyName = event.familyName ?: state.familyName,
      dateOfBirth = event.dateOfBirth ?: state.dateOfBirth,
    )

    private fun apply(state: PersonState, event: PersonUpdatedDetailsEvent) = PersonState(
      givenName = event.givenName ?: state.givenName,
      familyName = event.familyName ?: state.familyName,
      dateOfBirth = event.dateOfBirth ?: state.dateOfBirth,
    )

    private fun applyEvent(state: PersonState, event: EventEntity): PersonState {
      return when (event.eventType) {
        CREATED_PERSON -> apply(state, event.into<CreatedPersonEvent>())
        UPDATED_PERSON_DETAILS -> apply(state, event.into<PersonUpdatedDetailsEvent>())
        else -> state
      }
    }
  }
}
