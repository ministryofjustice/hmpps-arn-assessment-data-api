package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandResponse

@Service
class AddressCommandHandler(
  val address: Address,
) {
  fun handle(command: CreateAddressCommand): List<CommandResponse> {
    val createdEvent = address.handle(command)

    return listOf(createdEvent)
  }

  fun handle(command: ChangeAddressCommand): List<CommandResponse> {
    val changedEvent = address.handle(command)

    return listOf(changedEvent)
  }
}
