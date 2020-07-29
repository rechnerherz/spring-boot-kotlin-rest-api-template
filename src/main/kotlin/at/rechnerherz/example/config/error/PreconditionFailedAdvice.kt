package at.rechnerherz.example.config.error

import org.springframework.core.annotation.Order
import org.springframework.data.rest.webmvc.support.ETagDoesntMatchException
import org.springframework.http.ResponseEntity
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status

/**
 * Return [Status.PRECONDITION_FAILED] for optimistic locking failures.
 */
@ControllerAdvice
@Order(0)
class PreconditionFailedAdvice : BaseAdviceTrait {

    @ExceptionHandler
    fun handle(exception: ETagDoesntMatchException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.PRECONDITION_FAILED, exception, request)

    @ExceptionHandler
    fun handle(exception: ObjectOptimisticLockingFailureException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.PRECONDITION_FAILED, exception, request)

}
