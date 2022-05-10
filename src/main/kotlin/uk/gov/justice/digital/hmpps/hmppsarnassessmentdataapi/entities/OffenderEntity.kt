package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "offender", schema = "arnassessmentdata")
data class OffenderEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "first_name")
  val firstName: String? = null,

  @Column(name = "last_name")
  val lastName: String? = null,

  @Column(name = "crn")
  val crn: String,

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  val gender: Gender? = null,

  @Column(name = "created_date")
  val createdDate: LocalDateTime? = LocalDateTime.now(),

) : Serializable
