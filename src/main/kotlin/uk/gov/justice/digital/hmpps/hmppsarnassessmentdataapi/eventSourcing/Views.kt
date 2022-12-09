package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing

enum class Roles {
  BASIC,
  SUPPLIER,
  PROBATION,
}

class Views {
  interface Basic
  interface Supplier : Basic
  interface Probation : Supplier

  companion object {
    private val MAPPINGS = mapOf(
      Roles.BASIC to Basic::class.java,
      Roles.SUPPLIER to Supplier::class.java,
      Roles.PROBATION to Probation::class.java,
    )

    fun from(s: String) = MAPPINGS[Roles.valueOf(s)]
  }
}
