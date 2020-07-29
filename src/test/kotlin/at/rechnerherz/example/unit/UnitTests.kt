package at.rechnerherz.example.unit

import at.rechnerherz.example.base.BaseUnitTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class UnitTests : BaseUnitTest() {

    @Test
    fun `unit test`() {
        1 shouldBeEqualTo 1
    }

}
