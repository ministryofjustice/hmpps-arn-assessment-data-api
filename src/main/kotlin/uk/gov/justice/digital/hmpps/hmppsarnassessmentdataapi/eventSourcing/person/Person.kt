package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateStore
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType.PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CHANGES_APPROVED
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.CREATED_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.PERSON_MOVED_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType.UPDATED_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories.EventRepository
import java.time.LocalDate
import java.util.UUID

data class PersonDetails(
  val firstName: String,
  val familyName: String,
  val dateOfBirth: LocalDate,
)

data class PersonAddress(
  val addressId: UUID,
  val addressType: AddressType,
)

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
  fun create(personDetails: PersonDetails): CommandResponse {
    val aggregateId = aggregateStore.createAggregateRoot(PERSON)
    val event = EventEntity(
      aggregateId = aggregateId,
      eventType = CREATED_PERSON,
      values = mapOf(
        "given_name" to personDetails.firstName,
        "family_name" to personDetails.familyName,
        "date_of_birth" to personDetails.dateOfBirth.toString(),
      )
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun updateDetails(aggregateId: UUID, personDetails: PersonDetails): CommandResponse {
    val event = EventEntity(
      aggregateId = aggregateId,
      eventType = UPDATED_PERSON_DETAILS,
      values = mapOf(
        "given_name" to personDetails.firstName,
        "family_name" to personDetails.familyName,
        "date_of_birth" to personDetails.dateOfBirth.toString(),
      )
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun markAsApproved(aggregateId: UUID): CommandResponse {
    val event = EventEntity(
      aggregateId = aggregateId,
      eventType = CHANGES_APPROVED,
      values = emptyMap(),
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  fun moveAddress(aggregateId: UUID, personAddress: PersonAddress): CommandResponse {
    val event = EventEntity(
      aggregateId = aggregateId,
      eventType = PERSON_MOVED_ADDRESS,
      values = mapOf(
        "addressUuid" to personAddress.addressId.toString(),
        "addressType" to personAddress.addressType.toString(),
      )
    )

    eventRepository.save(event)

    return CommandResponse.from(event)
  }

  companion object {
    fun aggregate(events: List<EventEntity>) = events
      .sortedBy { it.createdOn }
      .fold(PersonState()) { state: PersonState, event: EventEntity -> applyEvent(state, event) }

    private fun applyEvent(state: PersonState, event: EventEntity): PersonState {
      return when (event.eventType) {
        CREATED_PERSON, UPDATED_PERSON_DETAILS -> {
          val dateOfBirth = event.values["date_of_birth"]

          PersonState(
            givenName = event.values.getOrDefault("given_name", state.givenName),
            familyName = event.values.getOrDefault("family_name", state.givenName),
            dateOfBirth = if (!dateOfBirth.isNullOrBlank()) LocalDate.parse(dateOfBirth) else state.dateOfBirth
          )
        }
        else -> state
      }
    }
  }
}
