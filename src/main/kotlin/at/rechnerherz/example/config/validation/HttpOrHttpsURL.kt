package at.rechnerherz.example.config.validation

import org.hibernate.validator.constraints.URL
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

const val HTTP_OR_HTTPS_PATTERN = """|https?.*"""

/**
 * - Must be a valid http:// or https:// URL.
 */
@Constraint(validatedBy = [])
@URL
@Pattern(regexp = HTTP_OR_HTTPS_PATTERN)
@ReportAsSingleViolation
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class HttpOrHttpsURL(
    val message: String = "{at.rechnerherz.example.config.validation.HttpOrHttpsURL.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
