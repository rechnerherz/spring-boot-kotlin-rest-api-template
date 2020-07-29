package at.rechnerherz.example.config.auth

import at.rechnerherz.example.domain.account.Role
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetails

/**
 * Retrieves the authentication from the current [org.springframework.security.core.context.SecurityContext], or null if unauthenticated.
 *
 * Unless anonymous authentication is disabled, this might return an [AnonymousAuthenticationToken].
 *
 * [Obtaining information about the current user](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#obtaining-information-about-the-current-user)
 */
fun authentication(): Authentication? =
    SecurityContextHolder.getContext().authentication

/**
 * Returns true if the current authentication is authenticated and not an instance of [AnonymousAuthenticationToken].
 */
fun isFullyAuthenticated(): Boolean =
    authentication()?.isFullyAuthenticated() == true

/**
 * Returns true if the current authentication is fully authenticated with the given role.
 */
fun isFullyAuthenticated(role: Role): Boolean =
    authentication()?.isFullyAuthenticated(role) == true

/**
 * Returns true if the [Authentication] is authenticated and not an instance of [AnonymousAuthenticationToken].
 */
fun Authentication?.isFullyAuthenticated(): Boolean =
    this !is AnonymousAuthenticationToken && this?.isAuthenticated == true

/**
 * Returns true if the [Authentication] is fully authenticated with the given [Role].
 */
fun Authentication?.isFullyAuthenticated(role: Role): Boolean =
    isFullyAuthenticated() && this?.authorities?.any { it.authority == role.authority } == true

/**
 * Return the remoteAddress from an [Authentication], or null if the authentication details are not [WebAuthenticationDetails].
 */
fun Authentication.remoteAddress(): String? =
    (details as? WebAuthenticationDetails)?.remoteAddress

/**
 * Return the username from an [Authentication], or null if the principal is not a [UserDetails].
 */
fun Authentication?.username(): String? =
    (this?.principal as? UserDetails)?.username
