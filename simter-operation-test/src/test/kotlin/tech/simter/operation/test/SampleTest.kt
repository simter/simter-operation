package tech.simter.operation.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class SampleTest {
  private val logger = LoggerFactory.getLogger(SampleTest::class.java)

  @Test
  fun test() {
    logger.debug("test log config")
    assertThat(1 + 1).isEqualTo(2)
  }
}