package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Must be
 *
 * - null,
 * - or a valid cron pattern according to [org.springframework.scheduling.support.CronSequenceGenerator.isValidExpression],
 * - or "-" ([org.springframework.scheduling.annotation.Scheduled.CRON_DISABLED]).
 *
 * I.e. second, minute, hour, day, month, and weekday.
 */
@Constraint(validatedBy = [])
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValidCronPattern(
    val message: String = "{at.rechnerherz.example.config.validation.ValidCronPattern.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
