package tech.simter.operation

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationView

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.core")
class ModuleConfiguration {
  @Bean
  fun serializersModule4Operation(): SerializersModule {
    return SerializersModule {
      polymorphic(Operation::class) {
        subclass(Operation.Impl::class)
      }
      polymorphic(Operation.Item::class) {
        subclass(Operation.Item.Impl::class)
      }
      polymorphic(OperationView::class) {
        subclass(OperationView.Impl::class)
      }

      // for Page<T>
      // https://github.com/simter/simter-kotlin/blob/master/src/test/kotlin/tech/simter/kotlin/serialization/PageTest.kt
      // https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/polymorphism.md#polymorphism-and-generic-classes
      polymorphic(Any::class) {
        subclass(Operation.Impl::class)
        subclass(Operation.Item.Impl::class)
        subclass(OperationView.Impl::class)
      }
    }
  }
}