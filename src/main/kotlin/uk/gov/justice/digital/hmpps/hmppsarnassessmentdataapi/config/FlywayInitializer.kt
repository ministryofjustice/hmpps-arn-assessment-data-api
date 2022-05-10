package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.config

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
class FlywayInitializer(@Qualifier("dataSource") dataSource: DataSource) {
  private val dataSource: DataSource = dataSource

  @PostConstruct
  fun migrateFlyway() {
    Flyway.configure()
      .schemas("arnassessmentdata")
      .dataSource(dataSource)
      .locations("classpath:db/migration")
      .load().migrate()
  }
}
