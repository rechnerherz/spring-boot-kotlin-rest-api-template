package at.rechnerherz.example.config.auth

import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.stereotype.Component

/**
 * After failed authentication with a [UsernamePasswordAuthenticationToken], track the failed attempt in the [LoginAttemptService].
 */
@Component
class AuthenticationFailureListener(
    private val loginAttemptService: LoginAttemptService
) : ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    override fun onApplicationEvent(event: AuthenticationFailureBadCredentialsEvent) {
        if (event.source is UsernamePasswordAuthenticationToken) {
            val ip: String = event.authentication.remoteAddress() ?: "[unknown]"
            val username: String = event.authentication.name
            loginAttemptService.loginFailed(ip, username)
        }
    }
}

