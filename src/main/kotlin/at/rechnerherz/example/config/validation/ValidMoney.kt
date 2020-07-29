package at.rechnerherz.example.config.validation

import org.hibernate.validator.constraints.Currency
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.Digits
import kotlin.reflect.KClass

/**
 * Must
 * - have at most 2 fractional and 6 integral digits,
 * - have one of the supported currencies.
 */
@Constraint(validatedBy = [])
@Currency(
    value = [
        "EUR",
        "USD"
    ]
)
@Digits(integer = 6, fraction = 2)
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
annotation class ValidMoney(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
