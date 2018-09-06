package tech.simter.operation.starter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

/**
 * @author RJ
 */
class SampleTest {
  private val logger = LoggerFactory.getLogger(SampleTest::class.java)

  @Test
  fun test() {
    logger.debug("test log config")
    assertEquals(2, 1 + 1)
  }
}