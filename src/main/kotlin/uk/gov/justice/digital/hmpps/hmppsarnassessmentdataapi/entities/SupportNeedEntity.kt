package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.dto.SupportNeedDto
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "support_need", schema = "arnassessmentdata")
data class SupportNeedEntity(
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "type")
  var type: SupportNeedsType? = null,

  @Column(name = "details")
  var details: String? = null

) : Serializable {

  companion object {

    fun from(supportNeedDtos: MutableList<SupportNeedDto>): MutableList<SupportNeedEntity> {
      return supportNeedDtos.map { from(it) }.toMutableList()
    }

    private fun from(supportNeedDto: SupportNeedDto): SupportNeedEntity {
      return SupportNeedEntity(
        type = supportNeedDto.type,
        details = supportNeedDto.details
      )
    }
  }
}
