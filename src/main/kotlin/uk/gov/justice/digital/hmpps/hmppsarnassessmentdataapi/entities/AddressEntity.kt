package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.StateType
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "address", schema = "arnassessmentdata")
data class AddressEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  val id: Long? = null,

  @Column(name = "uuid")
  val uuid: UUID = UUID.randomUUID(),

  @Column(name = "aggregate_id")
  val aggregateId: UUID,

  @Column(name = "aggregate_state")
  @Enumerated(EnumType.STRING)
  val state: StateType,

  @Column(name = "building")
  var building: String,

  @Column(name = "postcode")
  var postcode: String,
)
