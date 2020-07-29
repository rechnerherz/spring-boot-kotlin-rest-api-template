package at.rechnerherz.example.config.logback

import at.rechnerherz.example.util.errorDispatchMap
import at.rechnerherz.example.util.requestMap
import at.rechnerherz.example.util.sessionMap
import at.rechnerherz.example.util.userMap
import org.slf4j.MDC
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

/**
 * A servlet filter that inserts various values retrieved from the HTTP
 * request into the MDC.
 *
 * The values are removed after the request is processed.
 *
 * Based on [ch.qos.logback.classic.helpers.MDCInsertingServletFilter].
 *
 * [MDCInsertingServletFilter](https://logback.qos.ch/manual/mdc.html#mis)
 */
@WebFilter(filterName = "MDCFilter")
class MDCFilter : Filter {

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        insertIntoMDC(request)
        try {
            chain.doFilter(request, response)
        } finally {
            clearMDC(request)
        }
    }

    private fun insertIntoMDC(request: ServletRequest) {
        if (request is HttpServletRequest)
            request.requestMap().entries
                .union(request.sessionMap().entries)
                .union(request.userMap().entries)
                .union(request.errorDispatchMap().entries)

    }

    private fun clearMDC(request: ServletRequest) {
        if (request is HttpServletRequest)
            request.requestMap().keys
                .union(request.sessionMap().keys)
                .union(request.userMap().keys)
                .union(request.errorDispatchMap().keys)
                .forEach(MDC::remove)
    }

}
