package at.rechnerherz.example.config.auth

import org.springframework.security.core.AuthenticationException

/**
 * An [AuthenticationException] that is thrown by the [LoginAttemptDaoAuthenticationProvider]
 * when the username or IP is blocked.
 */
class LoginAttemptBlockedException(msg: String) : AuthenticationException(msg)
