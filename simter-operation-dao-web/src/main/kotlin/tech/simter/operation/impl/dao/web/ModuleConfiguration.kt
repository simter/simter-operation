package tech.simter.operation.impl.dao.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient
import tech.simter.operation.PACKAGE
import tech.simter.reactive.web.Utils.createWebClient

/** The WebClient instance bean name that register to spring context */
const val WEB_CLIENT_KEY = "$PACKAGE.webClient"

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.impl.dao.web.ModuleConfiguration")
@ComponentScan
class ModuleConfiguration(
  @Value("\${simter-operation.server-url:http://localhost:9014/operation}")
  private val serverUrl: String,
  @Value("\${proxy.host:#{null}}")
  private val proxyHost: String?,
  @Value("\${proxy.port:#{null}}")
  private val proxyPort: Int?
) {
  @Bean(WEB_CLIENT_KEY)
  @Primary
  fun webClient4SimterOperation(): WebClient {
    return createWebClient(
      baseUrl = serverUrl,
      proxyHost = proxyHost,
      proxyPort = proxyPort,
      secure = false,
      autoRedirect = false
      //,maxInMemorySize = 5 * 1024 // default 256KB, change to 5MB
    )
  }
}