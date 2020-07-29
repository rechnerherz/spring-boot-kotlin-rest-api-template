package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Must be blank or 2 uppercase letters (not null).
 */
@Constraint(validatedBy = [])
@NotNull
@Pattern(regexp = "|[A-Z]{2}")
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
annotation class ValidAlpha2CountryCode(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
