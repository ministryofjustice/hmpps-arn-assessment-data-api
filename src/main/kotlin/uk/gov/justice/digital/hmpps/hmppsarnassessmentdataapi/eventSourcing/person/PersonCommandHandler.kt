package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Command
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse
import java.time.LocalDate
import java.util.UUID

@Service
class PersonCommandHandler(
  val person: Person,
) {
  fun createPerson(command: Command): List<CommandResponse> {
    val createdEvent = person.create(
      PersonDetails(
        firstName = command.values["firstName"].orEmpty(),
        familyName = command.values["familyName"].orEmpty(),
        dateOfBirth = LocalDate.parse(command.values["dateOfBirth"].orEmpty())
      )
    )

    val approvedEvent = person.markAsApproved(createdEvent.aggregateId)

    return listOf(createdEvent, approvedEvent)
  }

  fun updatePersonDetails(command: Command): List<CommandResponse> {
    val updatedEvent = person.updateDetails(
      command.aggregateId!!,
      PersonDetails(
        firstName = command.values["firstName"].orEmpty(),
        familyName = command.values["familyName"].orEmpty(),
        dateOfBirth = LocalDate.parse(command.values["dateOfBirth"].orEmpty())
      )
    )

    return listOf(updatedEvent)
  }

  fun moveAddress(command: Command): List<CommandResponse> {
    val addressId = command.values["addressId"]
    val addressType = command.values["addressType"]

    if (command.aggregateId != null && !addressId.isNullOrBlank() && !addressType.isNullOrBlank()) {
      val movedAddressEvent = person.moveAddress(
        command.aggregateId,
        PersonAddress(
          addressId = UUID.fromString(addressId),
          addressType = AddressType.valueOf(addressType),
        )
      )

      return listOf(movedAddressEvent)
    }

    return emptyList()
  }

  fun approveChanges(command: Command): List<CommandResponse> {
    val approvedEvent = person.markAsApproved(command.aggregateId!!)

    return listOf(approvedEvent)
  }
}
