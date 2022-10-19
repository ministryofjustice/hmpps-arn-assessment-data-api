package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
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
  val uuid: UUID,
)
