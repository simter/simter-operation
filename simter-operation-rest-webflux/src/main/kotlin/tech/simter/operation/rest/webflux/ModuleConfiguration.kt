package tech.simter.operation.rest.webflux

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.web.reactive.function.server.router
import tech.simter.operation.PACKAGE
import tech.simter.operation.rest.webflux.handler.CreateHandler
import tech.simter.operation.rest.webflux.handler.FindByBatchHandler
import tech.simter.operation.rest.webflux.handler.FindByTargetHandler

/**
 * All configuration for this module.
 *
 * Register a `RouterFunction<ServerResponse>` with all routers for this module.
 * The default context-path of this router is '/operation'. And can be config by property `module.rest-context-path.simter-operation`.
 *
 * @author RJ
 * @author zh
 */
@Configuration("$PACKAGE.rest.webflux.ModuleConfiguration")
@ComponentScan("$PACKAGE.rest.webflux")
class ModuleConfiguration @Autowired constructor(
  @Value("\${module.version.simter-operation:UNKNOWN}") private val version: String,
  @Value("\${module.rest-context-path.simter-operation:/operation}") private val contextPath: String,
  private val findByBatchHandler: FindByBatchHandler,
  private val findByTargetHandler: FindByTargetHandler,
  private val createHandler: CreateHandler
) {
  private val logger = LoggerFactory.getLogger(ModuleConfiguration::class.java)

  init {
    logger.warn("module.version.simter-operation='{}'", version)
    logger.warn("module.rest-context-path.simter-operation='{}'", contextPath)
  }

  /** Register a `RouterFunction<ServerResponse>` with all routers for this module */
  @Bean("$PACKAGE.rest.webflux.Routes")
  @ConditionalOnMissingBean(name = ["$PACKAGE.rest.webflux.Routes"])
  fun operationRoutes() = router {
    contextPath.nest {
      // GET /target/{targetType}/{targetId} find Operations by target
      FindByTargetHandler.REQUEST_PREDICATE.invoke(findByTargetHandler::handle)
      // GET /batch/{batch} find Operations by batch
      FindByBatchHandler.REQUEST_PREDICATE.invoke(findByBatchHandler::handle)
      // POST / create Operation
      CreateHandler.REQUEST_PREDICATE.invoke(createHandler::handle)
      // GET /
      GET("/") { ok().contentType(TEXT_PLAIN).syncBody("simter-operation-$version") }
    }
  }
}