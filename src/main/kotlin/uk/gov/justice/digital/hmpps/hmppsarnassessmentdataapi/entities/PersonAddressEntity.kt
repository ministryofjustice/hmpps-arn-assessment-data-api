package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.AddressType
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "v_person_address", schema = "arnassessmentdata")
data class PersonAddressEntity(
  @Id
  @Column(name = "event_id")
  val eventId: UUID,

  @Column(name = "person_id")
  val personId: UUID,

  @Column(name = "created_on")
  val createdOn: LocalDateTime,

  @Column(name = "address_id")
  val addressId: UUID,

  @Column(name = "address_type")
  @Enumerated(EnumType.STRING)
  val addressType: AddressType,

  @Column(name = "building")
  var building: String,

  @Column(name = "postcode")
  var postcode: String,
)
