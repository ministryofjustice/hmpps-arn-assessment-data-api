package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.CommandEntity
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_UPDATE_ADDRESS_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.APPROVE_UPDATE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_NEW_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.CREATE_NEW_PERSON
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.MOVE_PERSONS_ADDRESS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.PROPOSE_UPDATE_ADDRESS_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.PROPOSE_UPDATE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.UPDATE_ADDRESS_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType.UPDATE_PERSON_DETAILS
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.Address
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.ApproveUpdateAddressDetailsCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.CreateNewAddressCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.ProposeUpdateAddressDetailsCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.address.UpdateAddressDetailsCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.ApproveUpdatePersonDetailsCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.CreateNewPersonCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.MovePersonAddressCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.Person
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.ProposeUpdatePersonDetailsCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.UpdatePersonDetailsCommand
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.JsonEventValues

enum class CommandType {
  CREATE_NEW_ADDRESS,
  UPDATE_ADDRESS_DETAILS,
  PROPOSE_UPDATE_ADDRESS_DETAILS,
  APPROVE_UPDATE_ADDRESS_DETAILS,
  CREATE_NEW_PERSON,
  UPDATE_PERSON_DETAILS,
  PROPOSE_UPDATE_PERSON_DETAILS,
  APPROVE_UPDATE_PERSON_DETAILS,
  MOVE_PERSONS_ADDRESS,
}

data class CommandRequest(
  val type: CommandType,
  val values: Map<String, String>,
) {
  inline fun <reified T> into(): T {
    return JsonEventValues.deserialize(JsonEventValues.serialize(values))!!
  }

  companion object {
    fun from(commandEntity: CommandEntity) = with(commandEntity) {
      CommandRequest(
        type = type,
        values = into(),
      )
    }
  }
}

@Service
class CommandHandler(
  val address: Address,
  val person: Person,
) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun handleAll(commands: List<CommandRequest>) = commands.map { handle(it) }.flatten()

  fun handle(command: CommandRequest) = with(command) {
    log.info("Command '$type' received'")
    when (type) {
      CREATE_NEW_ADDRESS -> address.handle(into<CreateNewAddressCommand>())
      PROPOSE_UPDATE_ADDRESS_DETAILS -> address.handle(into<ProposeUpdateAddressDetailsCommand>())
      APPROVE_UPDATE_ADDRESS_DETAILS -> address.handle(into<ApproveUpdateAddressDetailsCommand>())
      UPDATE_ADDRESS_DETAILS -> address.handle(into<UpdateAddressDetailsCommand>())
      CREATE_NEW_PERSON -> person.handle(into<CreateNewPersonCommand>())
      PROPOSE_UPDATE_PERSON_DETAILS -> person.handle(into<ProposeUpdatePersonDetailsCommand>())
      APPROVE_UPDATE_PERSON_DETAILS -> person.handle(into<ApproveUpdatePersonDetailsCommand>())
      UPDATE_PERSON_DETAILS -> person.handle(into<UpdatePersonDetailsCommand>())
      MOVE_PERSONS_ADDRESS -> person.handle(into<MovePersonAddressCommand>())
    }
  }.onEach { event -> log.info("Event '${event.eventType}' created for aggregate ID '${event.aggregateId}'") }
}
