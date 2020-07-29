package at.rechnerherz.example.integration

import at.rechnerherz.example.base.BaseIntegrationTest
import at.rechnerherz.example.base.WithMockAdmin
import at.rechnerherz.example.config.ACTUATOR_URL
import at.rechnerherz.example.config.API_URL
import at.rechnerherz.example.config.AUTHENTICATED_ACCOUNT_URL
import at.rechnerherz.example.config.JAVA_MELODY_URL
import at.rechnerherz.example.config.auth.authentication
import at.rechnerherz.example.config.auth.isFullyAuthenticated
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.account.Role
import at.rechnerherz.example.domain.base.GenericRepositoryService
import at.rechnerherz.example.domain.permission.WithPermissionRule
import at.rechnerherz.example.init.ADMIN_USERNAME
import at.rechnerherz.example.init.admin
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import javax.persistence.metamodel.EntityType
import kotlin.reflect.full.findAnnotation

class SecurityTests(
    @Autowired val accountRepository: AccountRepository,
    @Autowired val genericRepositoryService: GenericRepositoryService
) : BaseIntegrationTest() {

    @Test
    @WithMockAdmin
    fun `mock admin puts authentication into security context`() {
        authentication()?.name shouldBeEqualTo ADMIN_USERNAME
        isFullyAuthenticated(Role.ADMIN) shouldBeEqualTo true
        accountRepository.findAuthenticated() shouldBeEqualTo admin()
    }

    @Test
    @WithMockAdmin
    fun `nonexistent URL returns 401`() {
        jsonGet("/cthulhu").statusCode shouldBeEqualTo HttpStatus.UNAUTHORIZED
    }

    @Test
    fun `when unauthenticated, api returns 401`() {
        jsonGet(API_URL).statusCode shouldBeEqualTo HttpStatus.UNAUTHORIZED
    }

    @Test
    fun `when unauthenticated, actuator returns 401`() {
        jsonGet(ACTUATOR_URL).statusCode shouldBeEqualTo HttpStatus.UNAUTHORIZED
    }

    @Test
    fun `when unauthenticated, monitoring returns 401`() {
        jsonGet(JAVA_MELODY_URL).statusCode shouldBeEqualTo HttpStatus.UNAUTHORIZED
    }

    @Test
    fun `when unauthenticated, public-account returns 204`() {
        jsonGet(AUTHENTICATED_ACCOUNT_URL).statusCode shouldBeEqualTo HttpStatus.NO_CONTENT
    }

    @Test
    @WithMockAdmin
    fun `public-account returns authenticated account`() {
        jsonGet(AUTHENTICATED_ACCOUNT_URL).apply {
            statusCode shouldBeEqualTo HttpStatus.OK
            headers.contentType shouldBeEqualTo MediaType.APPLICATION_JSON
            body jsonPath "$.username" shouldBeEqualTo ADMIN_USERNAME
        }
    }

    fun `entity has a permission rule`(entity: EntityType<*>) {
        println(entity.name)
        entity.javaType.kotlin.findAnnotation<WithPermissionRule>() shouldNotBeEqualTo null
    }

    @TestFactory
    fun `all rest resource entities have a permission rule`(): List<DynamicTest> =
        genericRepositoryService.getRepositoryRestResources().mapNotNull { (entityType, repositoryRestResource) ->
            repositoryRestResource?.let {
                DynamicTest.dynamicTest(entityType.name) { `entity has a permission rule`(entityType) }
            }
        }

}
