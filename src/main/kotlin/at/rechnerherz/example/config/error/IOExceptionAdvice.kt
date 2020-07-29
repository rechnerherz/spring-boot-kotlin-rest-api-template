package at.rechnerherz.example.config.error

import com.google.common.base.Throwables
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status
import java.io.IOException

/**
 * Return [Status.INTERNAL_SERVER_ERROR] for [IOException]s.
 *
 * Except, if the exception was caused by a "Broken pipe" (ClientAbortException), the socket is closed and server cannot write a response anymore.
 */
@ControllerAdvice
@Order(0)
class IOExceptionAdvice : BaseAdviceTrait {

    @ExceptionHandler
    fun exceptionHandler(exception: IOException, request: NativeWebRequest): ResponseEntity<Problem>? =
        if (Throwables.getRootCause(exception).message == "Broken pipe")
            null // socket is closed, cannot return any response
        else create(Status.INTERNAL_SERVER_ERROR, exception, request)
}
