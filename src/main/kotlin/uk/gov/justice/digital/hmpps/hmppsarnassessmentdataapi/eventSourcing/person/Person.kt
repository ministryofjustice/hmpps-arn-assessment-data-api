package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATED_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.time.LocalDate
import java.util.UUID

enum class AddressType {
  PRIMARY_RESIDENCE,
  KNOWN_ADDRESS,
}

data class PersonState(
  val givenName: String? = null,
  val familyName: String? = null,
  val dateOfBirth: LocalDate? = null,
)

data class CreatePersonCommand(
  val givenName: String,
  val familyName: String,
  val dateOfBirth: LocalDate,
)

data class UpdatePersonCommand(
  val aggregateId: UUID,
  val givenName: String,
  val familyName: String,
  val dateOfBirth: LocalDate,
)

data class MovePersonAddressCommand(
  val aggregateId: UUID,
  val addressId: UUID,
  val addressType: AddressType,
)

data class ApprovePersonChangesCommand(
  val aggregateId: UUID,
)

@Service
class Person(
  val address: Address,
  val eventRepository: EventRepository,
  val aggregateStore: AggregateStore,
) {
  fun handle(command: CreatePersonCommand): CommandResponse {
    val aggregateId = aggregateStore.createAggregateRoot(PERSON)
    val event = PersonCreatedEvent(
      aggregateId,
      PersonDetailsValues(
        command.givenName,
        command.familyName,
        command.dateOfBirth,
      )
    ).toEventEntity()

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun handle(command: UpdatePersonCommand): CommandResponse {
    val event = PersonUpdatedEvent(
      command.aggregateId,
      PersonDetailsValues(
        command.givenName,
        command.familyName,
        command.dateOfBirth,
      )
    ).toEventEntity()

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun handle(command: ApprovePersonChangesCommand): CommandResponse {
    val event = ApprovedPersonChangesEvent(
      command.aggregateId,
    ).toEventEntity()

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun handle(command: MovePersonAddressCommand): CommandResponse {
    val event = PersonMovedAddressEvent(
      command.aggregateId,
      PersonAddressDetailsValues(
        command.addressId,
        command.addressType,
      )
    ).toEventEntity()

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  companion object {
    fun aggregate(events: List<EventEntity>) = events
      .sortedBy { it.createdOn }
      .fold(PersonState()) { state: PersonState, event: EventEntity -> applyEvent(state, event) }

    private fun apply(state: PersonState, event: PersonCreatedEvent) = PersonState(
      givenName = event.values.givenName,
      familyName = event.values.familyName,
      dateOfBirth = event.values.dateOfBirth,
    )

    private fun apply(state: PersonState, event: PersonUpdatedEvent) = PersonState(
      givenName = event.values.givenName,
      familyName = event.values.familyName,
      dateOfBirth = event.values.dateOfBirth,
    )

    private fun applyEvent(state: PersonState, eventEntity: EventEntity): PersonState {
      return when (eventEntity.eventType) {
        CREATED_PERSON -> apply(state, PersonCreatedEvent.fromEventEntity(eventEntity))
        UPDATED_PERSON_DETAILS -> apply(state, PersonUpdatedEvent.fromEventEntity(eventEntity))
        else -> state
      }
    }
  }
}
