package tech.simter.operation.rest.webflux.handler.operation

import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient.bindToRouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import reactor.core.publisher.Flux
import tech.simter.operation.rest.webflux.handler.PoUtil.Companion.randomOperation
import tech.simter.operation.rest.webflux.handler.PoUtil.Companion.randomString
import tech.simter.operation.rest.webflux.handler.operation.FindByClusterHandler.Companion.REQUEST_PREDICATE
import tech.simter.operation.service.OperationService

/**
 * Test [FindByClusterHandler]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(FindByClusterHandler::class)
@MockBean(OperationService::class)
class FindByClusterHandlerTest @Autowired constructor(
  private val handler: FindByClusterHandler,
  private val service: OperationService
) {
  @Test
  fun handle() {
    val client = bindToRouterFunction(route(REQUEST_PREDICATE, handler)).build()
    // mock
    val cluster = randomString()
    val operation1 = randomOperation(cluster)
    val operation2 = randomOperation(cluster)
    `when`(service.findByCluster(cluster)).thenReturn(Flux.just(operation1, operation2))

    // invoke
    val response = client.get().uri("/cluster/$cluster").exchange()

    // verify
    response.expectStatus().isOk
      .expectBody()
      .jsonPath("$[0].type").isEqualTo(operation1.type)
      .jsonPath("$[0].operator.id").isEqualTo(operation1.operator.id)
      .jsonPath("$[0].operator.name").isEqualTo(operation1.operator.name)
      .jsonPath("$[0].target.id").isEqualTo(operation1.target.id)
      .jsonPath("$[0].target.type").isEqualTo(operation1.target.type)
      .jsonPath("$[0].target.name").isEqualTo(operation1.target.name!!)
      .jsonPath("$[0].cluster").isEqualTo(operation1.cluster!!)
      .jsonPath("$[1].type").isEqualTo(operation2.type)
      .jsonPath("$[1].operator.id").isEqualTo(operation2.operator.id)
      .jsonPath("$[1].operator.name").isEqualTo(operation2.operator.name)
      .jsonPath("$[1].target.id").isEqualTo(operation2.target.id)
      .jsonPath("$[1].target.type").isEqualTo(operation2.target.type)
      .jsonPath("$[1].target.name").isEqualTo(operation2.target.name!!)
      .jsonPath("$[1].cluster").isEqualTo(operation2.cluster!!)
    verify(service).findByCluster(cluster)
  }
}