package tech.simter.operation.service

import com.ninjasquad.springmockk.MockkBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import tech.simter.operation.dao.OperationDao
import tech.simter.reactive.security.ModuleAuthorizer

/**
 * @author RJ
 */
@Configuration
@Import(ModuleConfiguration::class)
@MockkBean(OperationDao::class, ModuleAuthorizer::class)
class UnitTestConfiguration