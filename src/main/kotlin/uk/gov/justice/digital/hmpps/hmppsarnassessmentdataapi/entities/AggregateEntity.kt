package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.AggregateType
import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.FetchType.EAGER
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "aggregate", schema = "arnassessmentdata")
data class AggregateEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  val id: Long? = null,

  @Column(name = "uuid")
  val uuid: UUID = UUID.randomUUID(),

  @Column(name = "aggregate_type")
  @Enumerated(STRING)
  val aggregateType: AggregateType,

  @OneToMany(orphanRemoval = true, fetch = EAGER)
  @JoinColumn(name = "aggregate_id", referencedColumnName = "uuid")
  val events: List<EventEntity>
) : Serializable
