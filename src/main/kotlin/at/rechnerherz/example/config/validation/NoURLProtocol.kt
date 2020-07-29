package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Must not contain a URL protocol (`://`).
 */
@Constraint(validatedBy = [])
@Pattern(regexp = """|((?!://).)*""", flags = [Pattern.Flag.DOTALL])
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
annotation class NoURLProtocol(
    val message: String = "{at.rechnerherz.example.config.validation.NoURLProtocol.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
