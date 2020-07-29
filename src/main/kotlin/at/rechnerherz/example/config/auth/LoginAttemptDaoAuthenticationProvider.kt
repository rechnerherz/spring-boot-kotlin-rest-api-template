package at.rechnerherz.example.config.auth

import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Extends the default [DaoAuthenticationProvider] to add additional authentication checks.
 *
 * Retrieves [UserDetails] from the [UserDetailsService] and authenticates the user with a [PasswordEncoder].
 *
 * If [passwordEncoder] is not set, uses the default [org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder].
 *
 * Additionally checks for too many failed login attempts with the [LoginAttemptService].
 *
 * [How do I access the user's IP Address ...?](https://docs.spring.io/spring-security/site/faq/faq.html#faq-request-details-in-user-service)
 */
class LoginAttemptDaoAuthenticationProvider(
    private var loginAttemptService: LoginAttemptService,
    userDetailsService: UserDetailsService,
    passwordEncoder: PasswordEncoder? = null
) : DaoAuthenticationProvider() {

    init {
        super.setUserDetailsService(userDetailsService)
        if (passwordEncoder != null)
            super.setPasswordEncoder(passwordEncoder)
    }

    private val log = KotlinLogging.logger {}

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails,
        authentication: UsernamePasswordAuthenticationToken
    ) {
        super.additionalAuthenticationChecks(userDetails, authentication)

        val ip: String = authentication.remoteAddress() ?: "[unknown]"
        val username: String = authentication.name
        log.debug { "Login attempt for user '$username' from IP '$ip'" }

        if (loginAttemptService.isIPBlocked(ip))
            throw LoginAttemptBlockedException("Too many failed login attempts from IP '$ip'")

        if (loginAttemptService.isUsernameBlocked(username))
            throw LoginAttemptBlockedException("Too many failed login attempts for user '$username'")
    }

}
