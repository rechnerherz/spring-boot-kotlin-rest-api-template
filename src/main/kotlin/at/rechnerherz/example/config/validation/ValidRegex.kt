package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Must be a valid regular expression according to [java.util.regex.Pattern.compile].
 */
@Constraint(validatedBy = [])
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValidRegex(
    val message: String = "{at.rechnerherz.example.config.validation.ValidRegex.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
