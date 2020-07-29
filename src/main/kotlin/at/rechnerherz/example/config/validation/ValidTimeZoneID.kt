package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Must be a valid time zone ID available in [java.util.TimeZone.getAvailableIDs] (or null or blank).
 */
@Constraint(validatedBy = [])
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValidTimeZoneID(
    val message: String = "{at.rechnerherz.example.config.validation.ValidTimeZoneID.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
