package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.OffenderDto
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "offender", schema = "arnassessmentdata")
data class OffenderEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long,

  @Column(name = "first_name")
  var firstName: String?,

  @Column(name = "last_name")
  var lastName: String?,

  @Column(name = "crn")
  val crn: String,

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  var gender: Gender?,

  @Column(name = "created_date")
  val createdDate: LocalDateTime? = LocalDateTime.now(),

  @OneToMany(mappedBy = "offender", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
  val assessments: MutableList<AssessmentEntity> = mutableListOf(),

) : Serializable {
  companion object {
    fun update(offenderDto: OffenderDto, offenderEntity: OffenderEntity) {
      offenderEntity.run {
        firstName = offenderDto.firstName
        lastName = offenderDto.lastName
        gender = offenderDto.gender
      }
    }
  }
}
