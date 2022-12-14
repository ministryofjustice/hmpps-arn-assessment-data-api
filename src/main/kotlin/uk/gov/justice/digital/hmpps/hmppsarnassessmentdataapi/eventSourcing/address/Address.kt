package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.UPDATE_ADDRESS_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.ADDRESS_DETAILS_UPDATED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.APPROVED_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PROPOSED_CHANGES
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.read.AddressProjection
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.ApprovedPersonChangesEvent
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import javax.transaction.Transactional

@Service
class Address(
  val eventRepository: EventRepository,
  val aggregateStore: AggregateStore,
  val commandStore: CommandStore
) {
  @Transactional
  fun handle(command: CreateNewAddressCommand): List<CommandResponse> {
    val aggregateId = aggregateStore.createAggregateRoot(ADDRESS)
    val createdEvent = EventEntity.from(
      aggregateId = aggregateId,
      eventType = CREATED_ADDRESS,
      values = AddressCreatedEvent(),
    )

    eventRepository.save(createdEvent)

    val updatedEvent = with(command) {
      EventEntity.from(
        aggregateId = aggregateId,
        eventType = ADDRESS_DETAILS_UPDATED,
        values = AddressDetailsUpdatedEvent(
          building = building,
          postcode = postcode,
        ),
      )
    }

    eventRepository.save(updatedEvent)

    log.info("Created new address: $aggregateId")

    return listOf(
      CommandResponse.from(createdEvent),
      CommandResponse.from(updatedEvent),
    )
  }

  fun handle(command: ProposeUpdateAddressDetailsCommand): List<CommandResponse> {
    val commandUUID = with(command) {
      commandStore.save(
        aggregateId,
        UPDATE_ADDRESS_DETAILS,
        UpdateAddressDetailsCommand(
          aggregateId = aggregateId,
          building = building,
          postcode = postcode,
        )
      )
    }

    return listOf(CommandResponse(command.aggregateId, PROPOSED_CHANGES, mapOf("commandId" to commandUUID.toString())))
  }

  @Transactional
  fun handle(command: ApproveUpdateAddressDetailsCommand): List<CommandResponse> {
    val pendingCommandRequest = commandStore.getCommand(command.commandUUID)!!
    val pendingCommand = pendingCommandRequest.into<UpdateAddressDetailsCommand>()
    val addressDetailsUpdated = handle(pendingCommand).first()

    val approvedEvent = EventEntity.from(
      aggregateId = pendingCommand.aggregateId,
      eventType = APPROVED_CHANGES,
      values = ApprovedPersonChangesEvent()
    )

    eventRepository.save(approvedEvent)
    commandStore.removeCommand(command.commandUUID)

    return listOf(
      CommandResponse.from(approvedEvent),
      addressDetailsUpdated,
    )
  }

  fun handle(command: UpdateAddressDetailsCommand): List<CommandResponse> {
    val addressExists = aggregateStore.checkAggregateRootExists(command.aggregateId)

    val event = with(command) {
      EventEntity.from(
        aggregateId = aggregateId,
        eventType = ADDRESS_DETAILS_UPDATED,
        values = AddressDetailsUpdatedEvent(
          building = building,
          postcode = postcode,
        )
      )
    }

    // can we make this smarter? like a diff?
    if (addressExists) {
      eventRepository.save(event)
      log.info("Changed address: $command.aggregateId")
    }

    return listOf(CommandResponse.from(event))
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun createProjectionFrom(events: List<EventEntity>): AddressProjection {
      val address = events
        .sortedBy { it.createdOn }
        .fold(AddressProjection()) { projection: AddressProjection, event: EventEntity -> applyEvent(projection, event) }

      address.addMetaDataFrom(events)

      return address
    }

    private fun apply(projection: AddressProjection, event: AddressDetailsUpdatedEvent) = AddressProjection(
      building = event.building ?: projection.building,
      postcode = event.postcode ?: projection.postcode,
    )

    private fun applyEvent(projection: AddressProjection, eventEntity: EventEntity) = when (eventEntity.eventType) {
      ADDRESS_DETAILS_UPDATED -> apply(projection, eventEntity.into<AddressDetailsUpdatedEvent>())
      else -> projection // skip events that don't build the aggregate
    }
  }
}
