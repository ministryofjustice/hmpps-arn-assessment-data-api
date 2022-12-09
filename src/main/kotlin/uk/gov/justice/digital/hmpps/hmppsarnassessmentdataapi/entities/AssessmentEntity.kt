package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.Person
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.person.read.PersonState
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "assessment", schema = "arnassessmentdata")
data class AssessmentEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
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

  @Column(name = "updated_on_date")
  var updatedOnDate: LocalDateTime? = LocalDateTime.now(),

  @Column(name = "completed_date")
  val completedDate: LocalDateTime? = null,

  @ManyToOne
  @JoinColumn(name = "subject", referencedColumnName = "uuid")
  private val offender: AggregateEntity? = null
) {
  fun getOffender(): PersonState? {
    return offender?.let {
      Person.aggregateFrom(
        offender.events.filter { it.createdOn < updatedOnDate }
      )
    }
  }
}
