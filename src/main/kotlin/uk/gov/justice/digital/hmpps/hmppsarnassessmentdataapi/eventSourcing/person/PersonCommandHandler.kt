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
    val createdPersonEvent = person.handle(
      CreatePersonCommand(
        givenName = command.values["firstName"].orEmpty(),
        familyName = command.values["familyName"].orEmpty(),
        dateOfBirth = LocalDate.parse(command.values["dateOfBirth"].orEmpty())
      )
    )

    val approvedPersonEvent = person.handle(
      ApprovePersonChangesCommand(
        createdPersonEvent.aggregateId
      )
    )

    return listOf(createdPersonEvent, approvedPersonEvent)
  }

  fun updatePersonDetails(command: Command): List<CommandResponse> {
    val updatedPersonEvent = person.handle(
      UpdatePersonCommand(
        aggregateId = command.aggregateId!!,
        givenName = command.values["firstName"].orEmpty(),
        familyName = command.values["familyName"].orEmpty(),
        dateOfBirth = LocalDate.parse(command.values["dateOfBirth"].orEmpty())
      )
    )

    return listOf(updatedPersonEvent)
  }

  fun moveAddress(command: Command): List<CommandResponse> {
    val addressId = command.values["addressId"]
    val addressType = command.values["addressType"]

    if (command.aggregateId != null && !addressId.isNullOrBlank() && !addressType.isNullOrBlank()) {
      val personMovedAddressEvent = person.handle(
        MovePersonAddressCommand(
          aggregateId = command.aggregateId,
          addressId = UUID.fromString(addressId),
          addressType = AddressType.valueOf(addressType),
        )
      )

      return listOf(personMovedAddressEvent)
    }

    return emptyList()
  }

  fun approveChanges(command: Command): List<CommandResponse> {
    val approvedPersonChangesEvent = person.handle(
      ApprovePersonChangesCommand(command.aggregateId!!)
    )

    return listOf(approvedPersonChangesEvent)
  }
}
