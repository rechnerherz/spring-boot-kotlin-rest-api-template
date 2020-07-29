package at.rechnerherz.example.base

import at.rechnerherz.example.config.INTEGRATION_TEST_TAG
import at.rechnerherz.example.config.TESTING
import at.rechnerherz.example.util.json
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

//TODO: fix tests with mock authentication
/**
 * Base class for integration tests.
 *
 * All integration tests should extend from this to use the same context configuration so that the context can be cached/reused.
 *
 * - [SpringExtension] sets up the application context with the Spring TestContext Framework.
 * - [SpringBootTest] does auto-configuration, sets up a web environment and registers a [org.springframework.boot.test.web.client.TestRestTemplate].
 *
 * [Spring Testing](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html),
 * [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
 * [TestRestTemplate](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-rest-templates-test-utility)
 */
@ActiveProfiles(TESTING)
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@Tag(INTEGRATION_TEST_TAG)
abstract class BaseIntegrationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @BeforeAll
    fun beforeAll() {
        println(">> Running ${this::class.simpleName}")
    }

    @AfterAll
    fun afterAll() {
        println(">> Done    ${this::class.simpleName}")
    }

    fun jsonGet(url: String): ResponseEntity<String> =
        testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity<String>(HttpHeaders().json()))

    infix fun String?.jsonPath(path: String): String =
        JsonPath.read(this, path)

}
