package tech.simter.operation.rest.webflux.handler

import com.ninjasquad.springmockk.MockkBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import tech.simter.operation.core.OperationService

/**
 * All unit test config for this module.
 */
@Configuration
@EnableWebFlux
@ComponentScan("tech.simter")
@MockkBean(OperationService::class)
class UnitTestConfiguration