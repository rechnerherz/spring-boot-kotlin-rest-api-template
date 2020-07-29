package at.rechnerherz.example.config.auth

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Delegates [AuthenticationException] to Spring's [HandlerExceptionResolver].
 */
class AuthenticationFailureResolver(
    private val resolver: HandlerExceptionResolver
) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authenticationException: AuthenticationException
    ) {
        resolver.resolveException(request, response, null, authenticationException)
    }
}
