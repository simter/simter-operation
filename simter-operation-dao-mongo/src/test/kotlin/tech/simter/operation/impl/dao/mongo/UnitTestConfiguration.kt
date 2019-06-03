package tech.simter.operation.impl.dao.mongo

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author RJ
 */
@Configuration
@Import(
  tech.simter.mongo.ModuleConfiguration::class,
  tech.simter.operation.impl.dao.mongo.ModuleConfiguration::class
)
class UnitTestConfiguration