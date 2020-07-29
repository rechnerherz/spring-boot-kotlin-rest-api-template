package at.rechnerherz.example.config.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotBlank
import kotlin.reflect.KClass

/**
 * - Must not be null or blank.
 * - Must not contain any HTML.
 * - Must not contain a URL protocol (`://`).
 */
@Constraint(validatedBy = [])
@NotBlank
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
annotation class MandatoryPlainText(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
