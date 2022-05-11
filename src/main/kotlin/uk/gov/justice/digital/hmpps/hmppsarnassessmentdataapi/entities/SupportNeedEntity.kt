package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "support_need", schema = "arnassessmentdata")

class SupportNeedEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "type")
  val type: SupportNeedsType? = null,

  @Column(name = "details")
  val details: String? = null

) : Serializable

