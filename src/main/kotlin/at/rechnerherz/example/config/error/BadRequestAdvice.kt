package at.rechnerherz.example.config.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.core.annotation.Order
import org.springframework.data.repository.support.QueryMethodParameterConversionException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status

/**
 * Return [Status.BAD_REQUEST] for exceptions indicating bad requests.
 */
@ControllerAdvice
@Order(0)
class BadRequestAdvice : BaseAdviceTrait {

    /**
     * [IllegalArgumentException]: thrown manually or by classes like [org.springframework.util.Assert] to validate input.
     */
    @ExceptionHandler
    fun handle(exception: IllegalArgumentException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.BAD_REQUEST, exception, request)

    /**
     * [IllegalStateException]: thrown manually or by classes like [org.springframework.util.Assert] to validate input.
     */
    @ExceptionHandler
    fun handle(exception: IllegalStateException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.BAD_REQUEST, exception, request)

    /**
     * [QueryMethodParameterConversionException]: thrown when a request parameter cannot be converted to a Spring Data Rest query method parameter.
     */
    @ExceptionHandler
    fun handle(exception: QueryMethodParameterConversionException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.BAD_REQUEST, exception, request)

    /**
     * [JsonMappingException]: thrown when a JSON request parameter or body cannot be mapped to the target type.
     */
    @ExceptionHandler
    fun handle(exception: JsonMappingException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.BAD_REQUEST, exception, request)

    /**
     * [MissingKotlinParameterException]: thrown when a mandatory constructor parameter is null or missing.
     */
    @ExceptionHandler
    fun handle(exception: MissingKotlinParameterException, request: NativeWebRequest): ResponseEntity<Problem> =
        create(Status.BAD_REQUEST, exception, request)

}
