package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.UPDATE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.APPROVED_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_DETAILS_UPDATED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PROPOSED_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.time.LocalDate
import javax.transaction.Transactional

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
  val commandStore: CommandStore,
) {
  @Transactional
  fun handle(command: CreateNewPersonCommand): List<CommandResponse> {
    val aggregateId = aggregateStore.createAggregateRoot(PERSON)
    val createdEvent = EventEntity.from(
      aggregateId = aggregateId,
      eventType = CREATED_PERSON,
      values = CreatedPersonEvent()
    )

    eventRepository.save(createdEvent)

    val updatedEvent = EventEntity.from(
      aggregateId = aggregateId,
      eventType = PERSON_DETAILS_UPDATED,
      values = PersonDetailsUpdatedEvent(
        givenName = command.givenName,
        familyName = command.familyName,
        dateOfBirth = command.dateOfBirth,
      )
    )

    eventRepository.save(updatedEvent)

    return listOf(
      CommandResponse.from(createdEvent),
      CommandResponse.from(updatedEvent),
    )
  }

  fun handle(command: ProposeUpdatePersonDetailsCommand): List<CommandResponse> {
    val commandUUID = commandStore.save(
      command.aggregateId, UPDATE_PERSON_DETAILS,
      UpdatePersonDetailsCommand(
        aggregateId = command.aggregateId,
        givenName = command.givenName,
        familyName = command.familyName,
        dateOfBirth = command.dateOfBirth,
      )
    )

    return listOf(CommandResponse(command.aggregateId, PROPOSED_CHANGES, mapOf("commandId" to commandUUID.toString())))
  }

  @Transactional
  fun handle(command: ApproveUpdatePersonDetailsCommand): List<CommandResponse> {
    val pendingCommandRequest = commandStore.getCommand(command.commandUUID)!!
    val pendingCommand = pendingCommandRequest.into<UpdatePersonDetailsCommand>()
    val personDetailsUpdated = handle(pendingCommandRequest.into<UpdatePersonDetailsCommand>()).first()

    val approvedEvent = EventEntity.from(
      aggregateId = pendingCommand.aggregateId,
      eventType = APPROVED_CHANGES,
      values = ApprovedPersonChangesEvent()
    )

    eventRepository.save(approvedEvent)
    commandStore.removeCommand(command.commandUUID)

    return listOf(
      CommandResponse.from(approvedEvent),
      personDetailsUpdated,
    )
  }

  fun handle(command: UpdatePersonDetailsCommand): List<CommandResponse> {
    val event = EventEntity.from(
      aggregateId = command.aggregateId,
      eventType = PERSON_DETAILS_UPDATED,
      values = PersonDetailsUpdatedEvent(
        givenName = command.givenName,
        familyName = command.familyName,
        dateOfBirth = command.dateOfBirth,
      )
    )

    eventRepository.save(event)

    return listOf(CommandResponse.from(event))
  }

  fun handle(command: MovePersonAddressCommand): List<CommandResponse> {
    val event = EventEntity.from(
      aggregateId = command.aggregateId,
      eventType = PERSON_MOVED_ADDRESS,
      values = PersonMovedAddressEvent(
        addressUUID = command.addressId,
        addressType = command.addressType,
      )
    )

    eventRepository.save(event)

    return listOf(CommandResponse.from(event))
  }

  companion object {
    fun aggregateFrom(events: List<EventEntity>) = events
      .sortedBy { it.createdOn }
      .fold(PersonState()) { state: PersonState, event: EventEntity -> applyEvent(state, event) }

    private fun apply(state: PersonState, event: PersonDetailsUpdatedEvent) = PersonState(
      givenName = event.givenName ?: state.givenName,
      familyName = event.familyName ?: state.familyName,
      dateOfBirth = event.dateOfBirth ?: state.dateOfBirth,
    )

    private fun applyEvent(state: PersonState, event: EventEntity): PersonState {
      return when (event.eventType) {
        PERSON_DETAILS_UPDATED -> apply(state, event.into<PersonDetailsUpdatedEvent>())
        else -> state
      }
    }
  }
}
