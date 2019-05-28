package tech.simter.operation.dao.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import tech.simter.operation.PACKAGE

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.dao.jpa.ModuleConfiguration")
@ComponentScan("$PACKAGE.dao.jpa")
@EnableJpaRepositories("$PACKAGE.dao.jpa")
@EntityScan("$PACKAGE.po")
class ModuleConfiguration