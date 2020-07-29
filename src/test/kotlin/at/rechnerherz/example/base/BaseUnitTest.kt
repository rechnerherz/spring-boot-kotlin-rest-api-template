package at.rechnerherz.example.base

import at.rechnerherz.example.config.TESTING
import at.rechnerherz.example.config.UNIT_TEST_TAG
import org.junit.jupiter.api.Tag
import org.springframework.test.context.ActiveProfiles

/**
 * Base class for unit tests.
 *
 * Just sets the active profile. Runs without application context.
 */
@ActiveProfiles(TESTING)
@Tag(UNIT_TEST_TAG)
abstract class BaseUnitTest
