package tech.simter.operation.impl.service

import com.ninjasquad.springmockk.MockkBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import tech.simter.operation.core.OperationDao
import tech.simter.operation.support.ModuleConfiguration
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.reactive.security.ReactiveSecurityService

/**
 * @author RJ
 */
@Configuration
@Import(ModuleConfiguration::class)
@MockkBean(OperationDao::class, ModuleAuthorizer::class, ReactiveSecurityService::class)
class UnitTestConfiguration