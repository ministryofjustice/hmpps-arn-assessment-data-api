package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.StateType
import java.time.LocalDate
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "person", schema = "arnassessmentdata")
data class PersonEntity(
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

  @Column(name = "given_name")
  var givenName: String,

  @Column(name = "family_name")
  var familyName: String,

  @Column(name = "date_of_birth")
  var dateOfBirth: LocalDate,

  @ManyToMany(cascade = [CascadeType.ALL])
  @JoinTable(
    name = "person_address",
    joinColumns = [JoinColumn(name = "person_id", referencedColumnName = "uuid")],
    inverseJoinColumns = [JoinColumn(name = "address_id", referencedColumnName = "uuid")],
  )
  val addresses: Set<AddressEntity> = emptySet()
)
