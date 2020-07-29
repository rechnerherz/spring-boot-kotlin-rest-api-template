package at.rechnerherz.example.integration

import at.rechnerherz.example.base.BaseIntegrationTest
import at.rechnerherz.example.base.WithMockAdmin
import at.rechnerherz.example.domain.base.GenericRepositoryService
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class RestTests(
    @Autowired val buildProperties: BuildProperties,
    @Autowired val genericRepositoryService: GenericRepositoryService
) : BaseIntegrationTest() {

    @Test
    fun `public-version returns version`() {
        jsonGet("/public/version").apply {
            statusCode shouldBeEqualTo HttpStatus.OK
            headers.contentType shouldBeEqualTo MediaType.APPLICATION_JSON
            body jsonPath "$.version" shouldBeEqualTo buildProperties.version
        }
    }

    fun `api-repository returns OK`(path: String) {
        jsonGet("/api/$path").apply {
            statusCode shouldBeEqualTo HttpStatus.OK
            headers.contentType shouldBeEqualTo MediaType.APPLICATION_JSON
        }
    }

    @TestFactory
    @WithMockAdmin
    fun `as admin, api-repository returns OK`(): List<DynamicTest> =
        genericRepositoryService.getRepositoryRestResources().values.filterNotNull().mapNotNull {
            DynamicTest.dynamicTest(it.path) { `api-repository returns OK`(it.path) }
        }
}
