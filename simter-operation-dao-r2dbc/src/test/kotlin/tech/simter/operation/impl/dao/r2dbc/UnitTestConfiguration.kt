package tech.simter.operation.impl.dao.r2dbc

import io.r2dbc.client.R2dbc
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration
@Import(
  tech.simter.r2dbc.R2dbcConfiguration::class,
  tech.simter.operation.impl.dao.r2dbc.ModuleConfiguration::class,
  UnitTestConfiguration.Cfg::class
)
class UnitTestConfiguration {
  @Configuration
  class Cfg {
    @Bean
    fun r2dbc(connectionFactory: ConnectionFactory): R2dbc {
      return R2dbc(connectionFactory)
    }
  }
}