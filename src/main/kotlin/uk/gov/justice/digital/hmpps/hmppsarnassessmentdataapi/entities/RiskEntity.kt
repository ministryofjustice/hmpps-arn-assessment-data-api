package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

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
@Table(name = "risk", schema = "arnassessmentdata")
data class RiskEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  val id: Long? = null,

  @Column(name = "uuid")
  val uuid: UUID = UUID.randomUUID(),

  @Column(name = "aggregate_id")
  val aggregateId: UUID,

  @Column(name = "riskOne")
  var riskOne: Double?,

  @Column(name = "riskTwo")
  var riskTwo: Double?,

  @Column(name = "score")
  var score: Double,

  @Column(name = "risk_level")
  val level: String,

  @Column(name = "version")
  val version: String
)
