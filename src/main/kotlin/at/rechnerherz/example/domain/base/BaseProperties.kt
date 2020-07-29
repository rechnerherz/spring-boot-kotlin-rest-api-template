package at.rechnerherz.example.domain.base

import at.rechnerherz.example.config.validation.ValidLocale
import at.rechnerherz.example.config.validation.ValidTimeZoneID
import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

@ConfigurationProperties(prefix = "at.rechnerherz.example.base")
@Validated
class BaseProperties {

    /** The backend base URL. */
    @NotBlank
    @URL(protocol = "https")
    var backendUrl: String = ""

    /** The frontend base URL. */
    @NotBlank
    @URL(protocol = "https")
    var frontendUrl: String = ""

    /** Allowed CORS origins. */
    var corsAllowedOrigins: MutableList<String> = mutableListOf()

    /** Append a scheme (https://) if missing, and remove trailing slashes. */
    fun correctedCorsAllowedOrigins() =
        corsAllowedOrigins.map { (if (!it.startsWith("http")) "https://$it" else it).removeSuffix("/") }

    /** The name of the locale cookie. */
    @NotBlank
    var localeCookieName: String = ""

    /** The max-age of the locale cookie (default is 2^31 - 1 = 2147483647 = 2038-01-19 04:14:07). */
    @Positive
    var localeCookieMaxAge: Int = 2147483647

    /** The BCP 47 language tag (e.g. en-US) for the default locale to use when the user doesn't not specify one in the locale cookie. */
    @ValidLocale
    var defaultUserLocale: String = ""

    fun defaultUserLocale(): Locale =
        if (defaultUserLocale.isNotBlank()) Locale.forLanguageTag(defaultUserLocale) else Locale.getDefault()

    /** The default time zone ID to use when the user doesn't not specify one in the locale cookie. */
    @ValidTimeZoneID
    var defaultUserTimeZone: String = ""

    fun defaultUserTimeZone(): TimeZone =
        if (defaultUserTimeZone.isNotBlank()) TimeZone.getTimeZone(defaultUserTimeZone) else TimeZone.getDefault()

    /** Whether to create test data on startup. */
    var populateTestData: Boolean = false

    /** When set to true the application will exit after startup. */
    var exitAfterStartup: Boolean = false

    /** The directory to store all files. Must start with `file:` and end with a `/`.*/
    @Pattern(regexp = "file:.*/")
    var directory: String = ""

}
