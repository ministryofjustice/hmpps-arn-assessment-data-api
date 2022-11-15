package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.EventType
import java.time.LocalDateTime
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
@Table(name = "event", schema = "arnassessmentdata")
@TypeDefs(
  TypeDef(name = "json", typeClass = JsonStringType::class),
  TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
class EventEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  val id: Long? = null,

  @Column(name = "uuid")
  val uuid: UUID = UUID.randomUUID(),

  @Column(name = "created_on")
  val createdOn: LocalDateTime = LocalDateTime.now(),

  @Column(name = "created_by")
  val createdBy: String = "Unknown",

  @Column(name = "aggregate_id")
  val aggregateId: UUID,

  @Column(name = "event_type")
  @Enumerated(EnumType.STRING)
  val eventType: EventType,

  @Type(type = "json")
  @Column(columnDefinition = "jsonb", name = "event_values")
  val values: String
)
