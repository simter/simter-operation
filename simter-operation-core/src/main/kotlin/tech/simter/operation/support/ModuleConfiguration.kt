package tech.simter.operation.support

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import tech.simter.operation.support.PACKAGE
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.reactive.security.ReactiveSecurityService
import tech.simter.reactive.security.properties.ModuleAuthorizeProperties
import tech.simter.reactive.security.properties.PermissionStrategy.Allow

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.service.ModuleConfiguration")
@EnableConfigurationProperties
@ComponentScan("$PACKAGE.service")
class ModuleConfiguration {
  /**
   * Starter should config yml key `module.authorization.simter-operation` to support specific role control,
   * otherwise the [ModuleConfiguration.moduleAuthorizer] would allow anything default.
   */
  @Bean("$PACKAGE.service.ModuleAuthorizeProperties")
  @ConfigurationProperties(prefix = "module.authorization.simter-operation")
  fun moduleAuthorizeProperties(): ModuleAuthorizeProperties {
    return ModuleAuthorizeProperties(defaultPermission = Allow)
  }

  @Bean("$PACKAGE.service.ModuleAuthorizer")
  fun moduleAuthorizer(
    @Qualifier("$PACKAGE.service.ModuleAuthorizeProperties")
    properties: ModuleAuthorizeProperties,
    securityService: ReactiveSecurityService
  ): ModuleAuthorizer {
    return ModuleAuthorizer.create(properties, securityService)
  }
}