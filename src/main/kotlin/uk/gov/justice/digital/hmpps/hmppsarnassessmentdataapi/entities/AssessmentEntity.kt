package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.UpdateAssessmentDto
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "assessment_type")
  val assessmentType: String,

  @Column(name = "version")
  val version: String,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offender_id", referencedColumnName = "id")
  val offender: OffenderEntity,

  @OneToMany(mappedBy = "id", cascade = [CascadeType.ALL])
  @Column(name = "support_needs_id")
  var supportNeeds: MutableList<SupportNeedEntity> = mutableListOf(),

  @Column(name = "created_date")
  val createdDate: LocalDateTime? = LocalDateTime.now(),

  @Column(name = "completed_date")
  val completedDate: LocalDateTime? = null,

  ) : Serializable {

    fun update(updateAssessmentDto: UpdateAssessmentDto){
      this.supportNeeds = SupportNeedEntity.from(updateAssessmentDto.supportNeeds)
    }
  }