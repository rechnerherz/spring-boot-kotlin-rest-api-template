package at.rechnerherz.example.config.error

import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait

/**
 * Register the [SecurityAdviceTrait].
 *
 * [Problem Spring Web Security integration](https://github.com/zalando/problem-spring-web#security)
 */
@ControllerAdvice
@Order(0)
class SecurityAdvice : SecurityAdviceTrait {

    override fun log(throwable: Throwable, problem: Problem, request: NativeWebRequest, status: HttpStatus) {
        ExceptionHandling.log(throwable, request, status)
    }
}
