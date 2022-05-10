package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
  basePackages = ["uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.repositories"],
  entityManagerFactoryRef = "entityManager",
  transactionManagerRef = "transactionManager"
)
class PersistenceConfiguration : WebMvcConfigurer {

  @Value("\${spring.datasource.url}")
  private val dataSourceUrl: String = ""

  @Bean
  @Primary
  fun entityManager(): LocalContainerEntityManagerFactoryBean {
    val em = LocalContainerEntityManagerFactoryBean()
    em.dataSource = dataSource()
    em.setPackagesToScan(
      "uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.entities"
    )
    val vendorAdapter = HibernateJpaVendorAdapter()
    em.jpaVendorAdapter = vendorAdapter
    return em
  }

  @Primary
  @Bean
  fun transactionManager(): PlatformTransactionManager {
    val transactionManager = JpaTransactionManager()
    transactionManager.entityManagerFactory = entityManager().getObject()
    return transactionManager
  }

  @Primary
  @Bean(name = ["dataSource"])
  @ConfigurationProperties(prefix = "spring.datasource")
  fun dataSource(): DataSource? {
    return DataSourceBuilder
      .create()
      .url(dataSourceUrl)
      .build()
  }
}
