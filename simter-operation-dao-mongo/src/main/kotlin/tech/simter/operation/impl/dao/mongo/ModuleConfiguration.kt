package tech.simter.operation.impl.dao.mongo

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import tech.simter.operation.support.PACKAGE

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Import(tech.simter.mongo.ModuleConfiguration::class) // auto register converters
@Configuration("$PACKAGE.dao.reactive.mongo.ModuleConfiguration")
@EnableReactiveMongoRepositories("$PACKAGE.dao.reactive.mongo")
@ComponentScan("$PACKAGE.dao.reactive.mongo")
@EntityScan("$PACKAGE.po")
class ModuleConfiguration