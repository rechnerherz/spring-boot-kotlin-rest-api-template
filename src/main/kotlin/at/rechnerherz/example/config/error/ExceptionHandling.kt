package at.rechnerherz.example.config.error

import mu.KotlinLogging
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.TransactionSystemException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.spring.web.advice.ProblemHandling
import org.zalando.problem.violations.Violation
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

/**
 * Customize exception handling.
 *
 * Has lowest precedence order, so that all other [ControllerAdvice]s can be applied first.
 *
 * [Problems for Spring Web](https://github.com/zalando/problem-spring-web)
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class ExceptionHandling : ProblemHandling {

    companion object {

        private val log = KotlinLogging.logger {}

        fun log(throwable: Throwable, request: NativeWebRequest, status: HttpStatus) {
            when {
                throwable is ConstraintViolationException ->
                    log.warn("${status.reasonPhrase} returned for $request: ${throwable.toReportString()}")
                status.is4xxClientError ->
                    log.warn("${status.reasonPhrase} returned for $request", throwable)
                else ->
                    log.error("${status.reasonPhrase} returned for $request", throwable)
            }
        }

        private fun createViolation(violation: ConstraintViolation<*>): Violation =
            Violation(violation.propertyPath.toString(), violation.message + ": " + violation.invalidValue)

        private fun Violation.toReportString(): String =
            "'$field' $message"

        private fun ConstraintViolationException.toReportString(): String =
            if (constraintViolations.isEmpty()) ""
            else "Constraint Violations:\n - " +
                    constraintViolations.joinToString("\n - ") { createViolation(it).toReportString() }
    }

    /**
     * Enable causal chains.
     */
    override fun isCausalChainsEnabled(): Boolean =
        true

    /**
     * Also log the stack trace of 4xx errors.
     */
    override fun log(throwable: Throwable, problem: Problem, request: NativeWebRequest, status: HttpStatus) {
        log(throwable, request, status)
    }

    /**
     * Also include the invalid value when reporting constraint violations.
     */
    override fun createViolation(violation: ConstraintViolation<*>): Violation =
        ExceptionHandling.createViolation(violation)

    /**
     * Unwrap [TransactionSystemException]s that where caused by [ConstraintViolationException]s.
     */
    @ExceptionHandler
    fun handle(
        exception: TransactionSystemException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> =
        if (exception.cause?.cause is ConstraintViolationException)
            handleConstraintViolation(exception.cause?.cause as ConstraintViolationException, request)
        else
            create(exception, request)

}
