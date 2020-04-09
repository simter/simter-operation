package tech.simter.operation.starter

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.cors.reactive.CorsUtils
import org.springframework.web.reactive.config.*
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.router
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import tech.simter.operation.PACKAGE
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

/**
 * Application WebFlux Configuration.
 *
 * See [WebFlux config API](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-config-enable)
 *
 * @author RJ
 */
@Configuration("$PACKAGE.starter.AppConfiguration")
@EnableWebFlux
class AppConfiguration @Autowired constructor(
  @Value("\${simter-operation.rest-context-path}") private val contextPath: String,
  @Value("\${simter.jwt.require-authorized}") private val requireAuthorized: Boolean,
  @Value("\${server.port}") private val serverPort: String,
  @Value("\${logging.file}") private val loggingFile: String,
  @Value("\${simter-operation.version:UNKNOWN}") private val simterOperationVersion: String,
  @Value("\${simter-operation.dependency-version.simter:UNKNOWN}") private val simterVersion: String,
  @Value("\${simter-operation.dependency-version.kotlin:UNKNOWN}") private val kotlinVersion: String,
  @Value("\${simter-operation.dependency-version.spring-framework:UNKNOWN}") private val springFrameworkVersion: String,
  @Value("\${simter-operation.dependency-version.spring-boot:UNKNOWN}") private val springBootVersion: String
) {
  private final val logger = LoggerFactory.getLogger(AppConfiguration::class.java)

  init {
    if (logger.isInfoEnabled) {
      logger.info("simter-operation.rest-context-path={}", contextPath)
      logger.info("simter.jwt.require-authorized={}", requireAuthorized)
      logger.info("server.port={}", serverPort)
      logger.info("logging.file={}", loggingFile)
    }
  }

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

      /** See [Static Resources](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-config-static-resources) */
      override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static/**", "/favicon.ico")
          .addResourceLocations("classpath:/META-INF/resources/static/")
          .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
      }
    }
  }

  private val rootPage: String = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width">
      <title>simter-operation</title>
      <style>html{background-color:#000;color:#fff}</style>
    </head>
    <body>
      <h2>Simter Operation Micro Service</h2>
      <div>Start at : ${OffsetDateTime.now()}</div>
      <div>Version : $simterOperationVersion</div>
      <ul>
        <li>simter-$simterVersion</li>
        <li>kotlin-$kotlinVersion</li>
        <li>spring-$springFrameworkVersion</li>
        <li>spring-boot-$springBootVersion</li>
      </ul>
    </body>
    </html>
  """.trimIndent()

  /**
   * Other application routes.
   */
  @Bean
  fun rootRoutes() = router {
    "/".nest {
      // root /
      GET("/") { ok().contentType(TEXT_HTML).bodyValue(rootPage) }
      // '/favicon.ico'
      GET("/favicon.ico") {
        ok().body(BodyInserters.fromResource(ClassPathResource("META-INF/resources/static/favicon.ico")))
      }

      // OPTIONS /*
      OPTIONS("/**") { noContent().build() }
    }
  }

  /**
   * Enabled static file for CORS request.
   *
   * Just add Access-Control-Allow-Origin header.
   */
  @Bean
  fun corsFilter4StaticFile(): WebFilter {
    return WebFilter { exchange: ServerWebExchange, chain: WebFilterChain ->
      val request = exchange.request
      if (CorsUtils.isCorsRequest(request)                  // cross origin
        && !CorsUtils.isPreFlightRequest(request)           // not OPTION request
        && request.path.value().startsWith("/static/")) {   // only for static file dir
        // Add Access-Control-Allow-Origin header
        exchange.response.headers.add("Access-Control-Allow-Origin", request.headers.getFirst(HttpHeaders.ORIGIN))
      }
      chain.filter(exchange)
    }
  }
}