package at.rechnerherz.example.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.EnableAspectJAutoProxy

const val INTEGRATION_TEST_TAG = "integration"
const val UNIT_TEST_TAG = "unit"

/**
 * Test configuration.
 */
@TestConfiguration
@EnableAspectJAutoProxy
class TestConfig
