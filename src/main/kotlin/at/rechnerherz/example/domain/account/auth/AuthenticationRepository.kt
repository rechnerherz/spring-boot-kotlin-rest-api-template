package at.rechnerherz.example.domain.account.auth

import at.rechnerherz.example.domain.account.Account
import org.springframework.security.core.Authentication

/**
 * A custom repository for fetching accounts by [Authentication], implemented by [AuthenticationRepositoryImpl].
 *
 * [Custom implementations for Spring Data repositories](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.custom-implementations)
 */
interface AuthenticationRepository {

    /**
     * Returns the authenticated account from the current [org.springframework.security.core.context.SecurityContext], or null if unauthenticated.
     */
    fun findAuthenticated(): Account?

    /**
     * Returns the authenticated account from the given [Authentication], or null if unauthenticated.
     */
    fun findByAuthentication(authentication: Authentication?): Account?

    /**
     * Fetch an account by username, using [org.hibernate.Session.bySimpleNaturalId].
     */
    fun findByUsername(username: String): Account?
}
