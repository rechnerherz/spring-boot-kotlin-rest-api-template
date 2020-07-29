package at.rechnerherz.example.config.error

import org.springframework.http.HttpStatus
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.spring.web.advice.AdviceTrait

/**
 * An [AdviceTrait] with improved logging.
 */
interface BaseAdviceTrait : AdviceTrait {

    override fun log(throwable: Throwable, problem: Problem, request: NativeWebRequest, status: HttpStatus) {
        ExceptionHandling.log(throwable, request, status)
    }

}
