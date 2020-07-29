package at.rechnerherz.example.util

import ch.qos.logback.classic.ClassicConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.servlet.DispatcherServlet
import javax.servlet.DispatcherType
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

private const val USER = "user"
private const val SESSION_ID = "session.id"
private const val SESSION_CREATION_TIME = "session.creationTime"
private const val SESSION_LAST_ACCESSED_TIME = "session.lastAccessedTime"

fun HttpServletRequest.dispatcherServletException(): Throwable? =
    getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE) as? Throwable

fun HttpServletRequest.errorDispatchException(): Throwable? =
    getAttribute(RequestDispatcher.ERROR_EXCEPTION) as? Throwable

fun HttpServletRequest.errorDispatchMessage(): String? =
    getAttribute(RequestDispatcher.ERROR_MESSAGE) as? String

fun HttpServletRequest.errorDispatchRequestURI(): String? =
    getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as? String

fun HttpServletRequest.errorDispatchStatus(): Int? =
    getAttribute(RequestDispatcher.ERROR_STATUS_CODE) as? Int

fun HttpServletRequest.requestException(): Throwable? =
    dispatcherServletException() ?: errorDispatchException()

fun HttpServletRequest.errorDispatchMap(): Map<String, Any?> =
    if (dispatcherType == DispatcherType.ERROR)
        mapOf(
            RequestDispatcher.ERROR_MESSAGE to errorDispatchMessage(),
            RequestDispatcher.ERROR_REQUEST_URI to errorDispatchRequestURI(),
            RequestDispatcher.ERROR_STATUS_CODE to errorDispatchStatus()
        )
    else
        emptyMap()

fun HttpServletRequest.userMap(): Map<String, Any?> =
    mapOf(USER to userPrincipal?.name)

fun HttpServletRequest.sessionMap(): Map<String, Any?> =
    getSession(false).let {
        mapOf(
            SESSION_ID to it?.id,
            SESSION_CREATION_TIME to it?.creationTime,
            SESSION_LAST_ACCESSED_TIME to it?.lastAccessedTime
        )
    }

typealias HttpHeaderConstants = com.google.common.net.HttpHeaders

fun HttpServletRequest.requestMap(): Map<String, Any?> =
    mapOf(
        ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY to remoteHost,
        ClassicConstants.REQUEST_METHOD to method,
        ClassicConstants.REQUEST_REQUEST_URI to requestURI,
        ClassicConstants.REQUEST_QUERY_STRING to queryString,
        ClassicConstants.REQUEST_REQUEST_URL to requestURL?.toString(),
        HttpHeaderConstants.X_FORWARDED_FOR to getHeader(HttpHeaderConstants.X_FORWARDED_FOR),
        HttpHeaderConstants.USER_AGENT to getHeader(HttpHeaderConstants.USER_AGENT)
    )

/**
 * Set content type and accept headers to application/json.
 */
fun HttpHeaders.json(): HttpHeaders =
        apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }

/**
 * Set basic auth headers.
 */
fun HttpHeaders.basicAuth(username: String, password: String): HttpHeaders =
        apply {
            set(HttpHeaders.AUTHORIZATION, "Basic " + "$username:$password".base64encode())
        }
