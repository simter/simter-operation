package cn.gftaxi.traffic.accident.rest.webflux

import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.HandlerFunction

/**
 * 本模块的基本单元测试配置。
 *
 * 此配置会自动根据 spring bean 的配置生成一个 [WebTestClient] 实例，使用者可以直接通过自动注入方式来使用。
 * 由于 [WebTestClient] 实例是需要用到 [RouterFunction] 路由配置的，因此如果单元测试类是
 * 单纯测试 [HandlerFunction] 的情况下，需要在此单元测试类内声明一个相应的路由来测试，如：
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
  // 启动 Jackson 自动配置：自动配置 ObjectMapper 实例等
  JacksonAutoConfiguration::class,
  // 详见 simter-reactive-web 模块 README.md 文件的说明：全局注册默认精确到分钟的 java-time 序列化和反序列化配置等
  tech.simter.reactive.web.webflux.WebFluxConfiguration::class
)
class UnitTestConfiguration