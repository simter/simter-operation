package tech.simter.operation.impl.dao.jpa

import com.ninjasquad.springmockk.MockkBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import tech.simter.reactive.security.ReactiveSecurityService

/**
 * @author RJ
 */
@Configuration
@ComponentScan("tech.simter")
@MockkBean(ReactiveSecurityService::class)
class UnitTestConfiguration