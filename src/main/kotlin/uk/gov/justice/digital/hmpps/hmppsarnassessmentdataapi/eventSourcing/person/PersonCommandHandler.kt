package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse

@Service
class PersonCommandHandler(
  val person: Person,
) {
  fun handle(command: CreatePersonCommand): List<CommandResponse> {
    val createdPersonEvent = person.handle(command)

    return listOf(createdPersonEvent)
  }

  fun handle(command: UpdatePersonCommand): List<CommandResponse> {
    val updatedPersonEvent = person.handle(command)

    return listOf(updatedPersonEvent)
  }

  fun handle(command: MovePersonAddressCommand): List<CommandResponse> {
    val personMovedAddressEvent = person.handle(command)

    return listOf(personMovedAddressEvent)
  }
}
