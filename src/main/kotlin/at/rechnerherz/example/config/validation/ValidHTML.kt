package at.rechnerherz.example.config.validation

import org.jsoup.safety.Whitelist
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import kotlin.reflect.KClass

/**
 * Must not be null and must only contain HTML allowed by [SafeHtml.WhiteListType.RELAXED].
 *
 * Additionally allowed:
 * - `s` tag
 * - `a` tag with `target` attribute
 * - `img` tag with `src` attribute and `data` protocol
 */
@Constraint(validatedBy = [])
@SafeHtml(
    whitelistType = SafeHtml.WhiteListType.RELAXED,
    additionalTagsWithAttributes = [
        SafeHtml.Tag(name = "s"),
        SafeHtml.Tag(name = "a", attributes = ["target"]),
        SafeHtml.Tag(name = "img", attributesWithProtocols = [SafeHtml.Attribute(name = "src", protocols = ["data"])])
    ]
)
@NotNull
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
annotation class ValidHTML(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

val customWhitelist: Whitelist = Whitelist.relaxed()
    .addTags("s")
    .addAttributes("a", "target")
    .addProtocols("img", "src", "data")
