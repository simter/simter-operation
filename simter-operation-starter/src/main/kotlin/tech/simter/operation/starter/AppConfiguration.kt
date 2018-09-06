package tech.simter.operation.starter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import java.time.OffsetDateTime

/**
 * Application WebFlux Configuration.
 *
 * see [WebFlux config API](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-config-enable)
 *
 * @author RJ
 */
@Configuration("tech.simter.operation.starter.AppConfiguration")
@EnableWebFlux
class AppConfiguration @Autowired constructor(
  @Value("\${module.version.simter-operation:UNKNOWN}") private val version: String
) {
  /**
   * Register by method [DelegatingWebFluxConfiguration.setConfigurers].
   *
   * See [WebFlux config API](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-config-enable)
   */
  @Bean
  fun rootWebFluxConfigurer(): WebFluxConfigurer {
    return object : WebFluxConfigurer {
      /**
       * CORS config.
       *
       * See [Enabling CORS](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-cors)
       */
      override fun addCorsMappings(registry: CorsRegistry?) {
        // Enabling CORS for the whole application
        // By default all origins and GET, HEAD, and POST methods are allowed
        registry!!.addMapping("/**")
          .allowedOrigins("*")
          .allowedMethods("*")
          .allowedHeaders("Authorization", "Content-Type", "Content-Disposition")
          .exposedHeaders("Location")
          .allowCredentials(false)
          .maxAge(1800) // seconds
      }
    }
  }

  private val startTime = OffsetDateTime.now()
  private val rootPage: String = """
    <h2>Simter Operation Micro Service</h2>
    <div>Start at : $startTime</div>
    <div>Version : $version</div>
  """.trimIndent()

  /**
   * Other application routes.
   */
  @Bean
  fun rootRoutes() = router {
    "/".nest { GET("/") { ok().contentType(TEXT_HTML).syncBody(rootPage) } }
  }
}