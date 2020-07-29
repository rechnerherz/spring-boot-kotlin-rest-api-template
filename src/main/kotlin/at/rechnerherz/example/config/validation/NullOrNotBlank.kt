package at.rechnerherz.example.config.validation

import org.hibernate.validator.constraints.CompositionType
import org.hibernate.validator.constraints.ConstraintComposition
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Null
import kotlin.reflect.KClass

/**
 * May be null, but must not be blank.
 */
@Constraint(validatedBy = [])
@ConstraintComposition(CompositionType.OR)
@Null
@NotBlank
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
annotation class NullOrNotBlank(
    val message: String = "{at.rechnerherz.example.config.validation.NullOrNotBlank.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
