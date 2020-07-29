package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Must not be blank and must only contain lowercase ASCII letters, numbers and `-`.
 */
@Constraint(validatedBy = [])
@Pattern(regexp = "[a-z0-9-]+")
@NotBlank
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
annotation class ValidSlug(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
