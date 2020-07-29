package at.rechnerherz.example.config.auth

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Filter for CSRF protection with a client-side generated token in a custom header.
 *
 * Checks for all requests
 * - except GET, HEAD, TRACE or OPTIONS, and
 * - ignoring requests matching [ignoreMatcher]
 * that a custom header is present.
 *
 * [Use of Custom Request Headers](https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html#use-of-custom-request-headers),
 * [Stateless CSRF protection](https://blog.jdriven.com/2014/10/stateless-spring-security-part-1-stateless-csrf-protection/)
 */
class StatelessCsrfFilter(
    private val accessDeniedHandler: AccessDeniedHandler,
    private val ignoreMatcher: RequestMatcher
) : OncePerRequestFilter() {

    companion object {
        const val CSRF_HEADER = "X-CSRF-TOKEN"
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean =
        !CsrfFilter.DEFAULT_CSRF_MATCHER.matches(request) || ignoreMatcher.matches(request)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val csrfHeaderValue = request.getHeader(CSRF_HEADER)

        if (csrfHeaderValue.isNullOrBlank()) {
            accessDeniedHandler.handle(request, response, AccessDeniedException("Missing CSRF token in header"))
            return
        }

        filterChain.doFilter(request, response)
    }
}
