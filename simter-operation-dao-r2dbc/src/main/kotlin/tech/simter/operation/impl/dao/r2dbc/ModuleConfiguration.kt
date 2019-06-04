package tech.simter.operation.impl.dao.r2dbc

import io.r2dbc.client.R2dbc
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import tech.simter.operation.PACKAGE

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.impl.dao.r2dbc.ModuleConfiguration")
@EnableR2dbcRepositories
@ComponentScan
class ModuleConfiguration {
  @Bean
  @ConditionalOnMissingBean
  fun r2dbc(connectionFactory: ConnectionFactory): R2dbc {
    return R2dbc(connectionFactory)
  }
}