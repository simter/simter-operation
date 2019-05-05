package tech.simter.operation.rest.webflux.handler

import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.RouterFunction

/**
 * All unit test config for this module.
 *
 * This config will auto register a [WebTestClient] instance base on spring bean config,
 * and could be used for injection.
 *
 * Because [WebTestClient] requires [RouterFunction], so need to register a [RouterFunction]
 * on your unit test class. Such as:
 *
 * ```
 * @SpringJUnitConfig(UnitTestConfiguration::class, MyHandler::class)
 * @WebFluxTest
 * class MyHandlerTest @Autowired constructor(
 *   private val client: WebTestClient,
 *   private val myService: MyService
 * ) {
 *   @Configuration
 *   class Cfg {
 *     @Bean
 *     fun theRoute(handler: MyHandler): RouterFunction<ServerResponse> = route(REQUEST_PREDICATE, handler)
 *   }
 *
 *   @Test
 *   fun testMethod() {
 *     ...
 *   }
 * }
 * ```
 *
 * @author RJ
 */
@Configuration
@EnableWebFlux
@Import(
  // active Jackson auto config: auto register an ObjectMapper instance and so on.
  JacksonAutoConfiguration::class,
  // see also simter-reactive-web/README.md: global register java-time serialization and deserialization
  // accurate to minutes
  tech.simter.reactive.web.webflux.WebFluxConfiguration::class,
  // this module
  tech.simter.operation.rest.webflux.ModuleConfiguration::class
)
class UnitTestConfiguration