package tech.simter.operation.rest.webflux.handler

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux

/**
 * All unit test config for this module.
 */
@Configuration
@EnableWebFlux
@ComponentScan("tech.simter")
class UnitTestConfiguration