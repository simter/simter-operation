package tech.simter.operation.impl.dao.web

import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.reactive.security.ReactiveSecurityService
import tech.simter.reactive.web.Utils.createClientHttpConnector

/**
 * @author RJ
 */
@Configuration
@Import(ModuleConfiguration::class)
@MockkBean(ReactiveSecurityService::class)
class UnitTestConfiguration(
  @Value("\${simter-operation.server-url:http://localhost:8085/operation}")
  private val serverUrl: String,
  @Value("\${proxy.host:#{null}}")
  private val proxyHost: String?,
  @Value("\${proxy.port:#{null}}")
  private val proxyPort: Int?
) {
  @Bean
  fun webTestClient(): WebTestClient {
    return WebTestClient
      .bindToServer(
        createClientHttpConnector(
          proxyHost = proxyHost,
          proxyPort = proxyPort,
          connectTimeout = 30,
          readTimeout = 30,
          writeTimeout = 30
        )
      )
      .baseUrl(serverUrl)
      .build()
  }
}