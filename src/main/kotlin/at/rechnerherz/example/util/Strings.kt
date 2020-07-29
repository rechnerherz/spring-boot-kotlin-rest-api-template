package at.rechnerherz.example.util

import at.rechnerherz.example.config.validation.customWhitelist
import mu.KLogger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import org.jsoup.safety.Whitelist
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import kotlin.math.max

/**
 * Use += operator for [java.lang.StringBuilder.append].
 */
operator fun StringBuilder.plusAssign(string: String) {
    append(string)
}

/**
 * Append [string] to the [message], only if trace is enabled for the logger.
 */
fun KLogger.traceAppend(message: StringBuilder, string: String) {
    if (isTraceEnabled)
        message.append(string)
}

/**
 * Join a string sequence using the given separator, skipping null or blank values.
 */
fun Sequence<String?>.joinNotBlankToString(separator: String = ", "): String =
    filter { !it.isNullOrBlank() }.joinToString(separator).trim()

/**
 * Truncate a string to be [maxLength] characters long. If the string is truncated the result ends with [truncationIndicator].
 */
fun String.truncate(maxLength: Int, truncationIndicator: String = "..."): String =
    if (length <= maxLength) this
    else StringBuilder(maxLength)
        .append(this, 0, max(maxLength - truncationIndicator.length, 0))
        .append(truncationIndicator)
        .toString()

/**
 * Capitalize each word in a string.
 */
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.toLowerCase().capitalize() }

/**
 * Clean an HTML body string.
 */
fun String?.cleanHTML(): String =
    Jsoup.clean(this ?: "", "", customWhitelist)

/**
 * Clean an HTML body string and output valid XHTML.
 */
fun String?.cleanXHTML(): String =
    Jsoup.clean(this ?: "", "", customWhitelist, Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml))

/**
 * Clean an HTML string.
 */
fun String?.toPlainText(): String =
    Jsoup.clean(this ?: "", "", Whitelist.none(), Document.OutputSettings().prettyPrint(false))

/**
 * Convert the HTML body to plain text.
 */
fun String?.toFormattedPlainText(): String =
    HtmlToPlainText().toPlainText(this ?: "").unescapeHTMLEntities()

/**
 * Convert the HTML body to plain text and truncate it to [maxLength] characters.
 */
fun String?.toTruncatedPlainText(maxLength: Int = 255): String =
    toFormattedPlainText()
        .replace("\n", " ")
        .trim()
        .truncate(maxLength)

/**
 * Unescape HTML entities from a string.
 */
fun String.unescapeHTMLEntities(): String =
    Parser.unescapeEntities(this, false)

/**
 * Returns a substring before the first occurrence of any [delimiters].
 * If the string does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original string.
 */
fun String.substringBeforeAny(delimiters: Collection<String>, missingDelimiterValue: String = this): String {
    val index = indexOfAny(delimiters)
    return if (index == -1) missingDelimiterValue else substring(0, index)
}

/**
 * Wrap an HTML body and title in a full HTML document.
 */
fun String.wrapHTMLBody(title: String): String = """
   |<html>
   |<head>
   |    <title>$title</title>
   |</head>
   |<body>
   |$this
   |</body>
   |</html>""".trimMargin()

/**
 * Encode to a base64 string.
 */
fun String.base64encode(): String =
    Base64.getEncoder().encodeToString(toByteArray())

/**
 * Decode a base64 string.
 */
fun String.base64decode(): String =
    String(Base64.getDecoder().decode(this))

/**
 * Build a URL with [path] relative to [base], and query params [params] and URL-encode it.
 */
fun buildURL(base: String, path: String, vararg params: Pair<String, String?>): String =
    buildUriComponents(base, path, *params).toUriString()

/**
 * Build a URL with [path] relative to [base], and query params [params] and URL-encode it.
 */
fun buildUriComponents(base: String, path: String, vararg params: Pair<String, String?>): UriComponents =
    UriComponentsBuilder
        .fromHttpUrl(base)
        .path(path)
        .also { builder ->
            params.forEach {
                builder.queryParam(it.first, it.second)
            }
        }
        .encode()
        .build()

/**
 * Append a path to the given URI string, ensuring they are separated by exactly one slash.
 *
 * e.g. `"foo/".appendPath("/bar")` and `"foo".appendPath("bar")` both return `"foo/bar"`
 */
fun String.appendPath(path: String): String =
    removeSuffix("/") + "/" + path.removePrefix("/")

/**
 * Returns the string ending with [suffix].
 *
 * If it doesn't end with suffix, it is appended.
 * If it ends with a part of suffix, the rest is appended.
 * If it already ends with the suffix, it is returned as is.
 *
 * e.g. `"foo".appendIfNotEndsWith(": ")` and `"foo:".appendIfNotEndsWith(": ")` both return `"foo: ".
 */
fun String.ensureEndsWith(suffix: String): String {
    var base = this
    suffix.reversed().forEach { base = base.removeSuffix(it.toString()) }
    return base + suffix
}

/**
 * Generate a unique name of the form "name (counter)",
 * by calling the [check] function and incrementing the counter until the result returned is `null`.
 */
fun uniqueName(name: String, limit: Int = 100, check: (name: String) -> Any?): String {
    var counter = 1
    var uniqueName = name
    while (counter < limit && check(uniqueName) != null)
        uniqueName = "$name (${++counter})"
    return uniqueName
}

/**
 * Generate a unique name of the form "name (counter)",
 * by calling the [check] function and incrementing the counter until the result returned is `null` or the given [self].
 */
fun uniqueName(name: String, self: Any, limit: Int = 100, check: (name: String) -> Any?): String {
    var counter = 1
    var uniqueName = name
    while (counter < limit && check(uniqueName).let { it != null && it != self })
        uniqueName = "$name (${++counter})"
    return uniqueName
}
