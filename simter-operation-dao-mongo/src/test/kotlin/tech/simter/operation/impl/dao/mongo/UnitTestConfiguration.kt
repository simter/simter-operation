package tech.simter.operation.impl.dao.mongo

import com.ninjasquad.springmockk.MockkBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import tech.simter.reactive.security.ReactiveSecurityService

/**
 * @author RJ
 */
@Configuration
@Import(
  tech.simter.mongo.ModuleConfiguration::class,
  tech.simter.operation.impl.dao.mongo.ModuleConfiguration::class
)
@MockkBean(ReactiveSecurityService::class)
class UnitTestConfiguration