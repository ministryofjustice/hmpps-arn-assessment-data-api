package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "assessment", schema = "arnassessmentdata")
data class AssessmentEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "assessment_type")
  val assessmentType: String,

  @Column(name = "version")
  val version: String,

  @OneToMany(mappedBy = "id")
  @Column(name = "support_needs_id")
  var supportNeeds: MutableList<SupportNeedEntity> = mutableListOf(),

  @Column(name = "created_date")
  val createdDate: LocalDateTime? = LocalDateTime.now(),

  @Column(name = "completed_date")
  val completedDate: LocalDateTime? = null,

) : Serializable
