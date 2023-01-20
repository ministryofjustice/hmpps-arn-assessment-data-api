package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

import com.fasterxml.jackson.annotation.JsonView
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities.EventEntity
import java.time.LocalDateTime

data class MetaData(
  @field:JsonView(Views.Probation::class)
  var numberOfContributors: Int = 0,

  @field:JsonView(Views.Probation::class)
  var numberOfChanges: Int = 0,

  @field:JsonView(Views.Probation::class)
  var lastEditedBy: String? = null,

  @field:JsonView(Views.Basic::class)
  var lastEditedOn: LocalDateTime? = null,
)

abstract class Projection(
  @field:JsonView(Views.Basic::class)
  private val metaData: MetaData = MetaData(),
) {
  fun addMetaDataFrom(events: List<EventEntity>) {
    with(metaData) {
      lastEditedBy = events.last().createdBy
      lastEditedOn = events.last().createdOn
      numberOfChanges = events.size
      numberOfContributors = events.map { event -> event.createdBy }.toSet().size
    }
  }
}
