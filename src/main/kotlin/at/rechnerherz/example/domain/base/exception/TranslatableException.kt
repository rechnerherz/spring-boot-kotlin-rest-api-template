package at.rechnerherz.example.domain.base.exception

/**
 * An abstract base class for exception with a message and a translation key and options.
 *
 * The message is for backend logs. The key and options should be passed directly to the i18next translation function in the frontend.
 */
abstract class TranslatableException(
    message: String,
    val key: String,
    val options: Map<String, String>,
    cause: Throwable? = null
) : RuntimeException(message, cause)

