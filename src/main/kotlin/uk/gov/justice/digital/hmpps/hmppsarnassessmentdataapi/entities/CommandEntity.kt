package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.CommandType
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.utils.JsonEventValues
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
@Table(name = "command", schema = "arnassessmentdata")
@TypeDefs(
  TypeDef(name = "json", typeClass = JsonStringType::class),
  TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
class CommandEntity(
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  val id: Long? = null,

  @Column(name = "uuid")
  val uuid: UUID = UUID.randomUUID(),

  @Column(name = "aggregate_id")
  val aggregateId: UUID,

  @Column(name = "command_type")
  @Enumerated(EnumType.STRING)
  val type: CommandType,

  @Type(type = "json")
  @Column(columnDefinition = "jsonb", name = "command_values")
  val values: String
) {
  final inline fun <reified T : Any> into(): T {
    return JsonEventValues.deserialize(values)!!
  }

  companion object {
    fun from(aggregateId: UUID, commandType: CommandType, values: Any = emptyMap<String, String>()) = CommandEntity(
      aggregateId = aggregateId,
      type = commandType,
      values = JsonEventValues.serialize(values),
    )
  }
}
