package tech.simter.operation.rest.webflux

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Page
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.web.reactive.function.server.router
import tech.simter.operation.PACKAGE
import tech.simter.operation.rest.webflux.handler.*

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
@ComponentScan
class ModuleConfiguration @Autowired constructor(
  @Value("\${module.version.simter-operation:UNKNOWN}") private val version: String,
  @Value("\${module.rest-context-path.simter-operation:/operation}") private val contextPath: String,
  private val findHandler: FindHandler,
  private val findByBatchHandler: FindByBatchHandler,
  private val findByTargetHandler: FindByTargetHandler,
  private val createHandler: CreateHandler,
  private val getByIdHandler: GetByIdHandler
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
      // GET /?batch=x&target-type=x&target-id=x&search=x&page-no=x&page-size=x find pageable operations
      FindHandler.REQUEST_PREDICATE.invoke(findHandler::handle)
      // GET /target/{targetType}/{targetId} find Operations by target
      FindByTargetHandler.REQUEST_PREDICATE.invoke(findByTargetHandler::handle)
      // GET /batch/{batch} find Operations by batch
      FindByBatchHandler.REQUEST_PREDICATE.invoke(findByBatchHandler::handle)
      //GET /{id} get one operation by id
      GetByIdHandler.REQUEST_PREDICATE.invoke(getByIdHandler::handle)
      // POST / create Operation
      CreateHandler.REQUEST_PREDICATE.invoke(createHandler::handle)
      // GET /
      GET("/") { ok().contentType(TEXT_PLAIN).bodyValue("simter-operation-rest-webflux-$version") }
    }
  }
}

// Convert [Page] to a platform-specific [Map] structure
fun <T : Any> Page<T>.convert(): Map<String, Any?> = mapOf(
  "count" to this.totalElements,
  "pageNo" to this.pageable.pageNumber + 1,
  "pageSize" to this.pageable.pageSize,
  "rows" to this.content
)