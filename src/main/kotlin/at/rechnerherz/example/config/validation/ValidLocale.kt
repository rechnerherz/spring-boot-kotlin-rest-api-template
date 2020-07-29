package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Must be a valid locale language tag according to [java.util.Locale.Builder.setLanguageTag] (or null or blank).
 */
@Constraint(validatedBy = [])
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValidLocale(
    val message: String = "{at.rechnerherz.example.config.validation.ValidLocale.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
