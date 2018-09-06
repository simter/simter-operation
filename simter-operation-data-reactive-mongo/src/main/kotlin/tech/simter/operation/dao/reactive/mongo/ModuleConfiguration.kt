package tech.simter.operation.dao.reactive.mongo

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

private const val MODULE = "tech.simter.operation"

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Import(tech.simter.mongo.ModuleConfiguration::class) // auto register converters
@Configuration("$MODULE.dao.reactive.mongo.ModuleConfiguration")
@EnableReactiveMongoRepositories("$MODULE.dao.reactive.mongo")
@ComponentScan("$MODULE.dao.reactive.mongo")
@EntityScan("$MODULE.po")
class ModuleConfiguration