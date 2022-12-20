package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.ChangeDto
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.UPDATE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.APPROVED_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_DETAILS_UPDATED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PROPOSED_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read.PersonProjection
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.util.Objects
import java.util.UUID
import javax.transaction.Transactional
import kotlin.reflect.full.memberProperties

enum class AddressType {
  PRIMARY_RESIDENCE,
  KNOWN_ADDRESS,
}

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

    val updatedEvent = with(command) {
      EventEntity.from(
        aggregateId = aggregateId,
        eventType = PERSON_DETAILS_UPDATED,
        values = PersonDetailsUpdatedEvent(
          givenName = givenName,
          familyName = familyName,
          dateOfBirth = dateOfBirth,
        )
      )
    }

    eventRepository.save(updatedEvent)

    return listOf(
      CommandResponse.from(createdEvent),
      CommandResponse.from(updatedEvent),
    )
  }

  fun handle(command: ProposeUpdatePersonDetailsCommand): List<CommandResponse> {
    val commandUUID = with(command) {
      commandStore.save(
        aggregateId,
        UPDATE_PERSON_DETAILS,
        UpdatePersonDetailsCommand(
          aggregateId = aggregateId,
          givenName = givenName,
          familyName = familyName,
          dateOfBirth = dateOfBirth,
        )
      )
    }

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
    val event = with(command) {
      EventEntity.from(
        aggregateId = aggregateId,
        eventType = PERSON_DETAILS_UPDATED,
        values = PersonDetailsUpdatedEvent(
          givenName = givenName,
          familyName = familyName,
          dateOfBirth = dateOfBirth,
        )
      )
    }

    eventRepository.save(event)

    return listOf(CommandResponse.from(event))
  }

  fun handle(command: MovePersonAddressCommand): List<CommandResponse> {
    val event = with(command) {
      EventEntity.from(
        aggregateId = aggregateId,
        eventType = PERSON_MOVED_ADDRESS,
        values = PersonMovedAddressEvent(
          addressUUID = addressId,
          addressType = addressType,
        )
      )
    }

    eventRepository.save(event)

    return listOf(CommandResponse.from(event))
  }

  companion object {
    fun getChangesForEvent(eventId: UUID, events: List<EventEntity>): List<ChangeDto> {
      return events.find { it.uuid == eventId }?.let { event ->
        val proposed = createProjectionFrom(listOf(event))

        val eventsBefore = events.sortedBy { it.createdOn }.slice(0 until events.indexOf(event))
        val current = createProjectionFrom(eventsBefore, false)

        return PersonProjection::class.memberProperties
          .filter {
            !Objects.equals(it.get(proposed), it.get(current))
          }
          .map {
            ChangeDto(
              field = it.name,
              from = it.get(current).toString(),
              to = it.get(proposed).toString()
            )
          }
      } ?: emptyList()
    }

    fun createProjectionFrom(events: List<EventEntity>, includeMetaData: Boolean? = true): PersonProjection {
      val person = events
        .sortedBy { it.createdOn }
        .fold(PersonProjection()) { projection: PersonProjection, event: EventEntity -> applyEvent(projection, event) }

      if (includeMetaData == true) {
        person.addMetaDataFrom(events)
      }

      return person
    }

    private fun apply(projection: PersonProjection, event: PersonDetailsUpdatedEvent) = PersonProjection(
      givenName = event.givenName ?: projection.givenName,
      familyName = event.familyName ?: projection.familyName,
      dateOfBirth = event.dateOfBirth ?: projection.dateOfBirth,
    )

    private fun applyEvent(projection: PersonProjection, event: EventEntity) = when (event.eventType) {
      PERSON_DETAILS_UPDATED -> apply(projection, event.into<PersonDetailsUpdatedEvent>())
      else -> projection
    }
  }
}
