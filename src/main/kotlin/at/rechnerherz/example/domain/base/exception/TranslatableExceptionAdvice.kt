package at.rechnerherz.example.domain.base.exception

import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.ProblemBuilder
import org.zalando.problem.Status
import org.zalando.problem.StatusType
import org.zalando.problem.spring.web.advice.AdviceTrait
import java.net.URI

/**
 * Return [Status.CONFLICT] for [TranslatableException]s, adding a translation key and options to the problem.
 */
@ControllerAdvice
@Order(0)
class TranslatableExceptionAdvice : AdviceTrait {

    @ExceptionHandler
    fun handle(exception: TranslatableException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.CONFLICT, exception, request)

    override fun prepare(throwable: Throwable, status: StatusType, type: URI): ProblemBuilder {
        val problemBuilder = super.prepare(throwable, status, type)
        if (throwable is TranslatableException) {
            problemBuilder.with("key", throwable.key)
            problemBuilder.with("options", throwable.options)
        }
        return problemBuilder
    }
}
