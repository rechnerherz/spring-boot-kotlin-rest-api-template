package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * - May be null but not blank.
 * - Must not contain any HTML.
 * - Must not contain a URL protocol (`://`).
 */
@Constraint(validatedBy = [])
@NullOrNotBlank
@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
@NoURLProtocol
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
annotation class NullablePlainText(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
