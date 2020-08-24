package at.rechnerherz.example.util

import org.springframework.context.MessageSource
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Return the ISO alpha-3 country code from a locale, or null if not available
 * (instead of throwing a [MissingResourceException]).
 */
val Locale.iso3CountryOrNull
    get() = try {
        isO3Country
    } catch (e: MissingResourceException) {
        null
    }

/**
 * Return a [Locale] for the given ISO alpha-2 country code, or null if not available.
 */
fun localeFromAlpha2CountryCode(alpha2CountryCode: String?): Locale? =
    if (alpha2CountryCode.isNullOrBlank()) null else Locale.getAvailableLocales().find { it.country == alpha2CountryCode }

/**
 * Return a [Locale] for the given ISO alpha-3 country code, or null if not available.
 */
fun localeFromAlpha3CountryCode(alpha3CountryCode: String?): Locale? =
    if (alpha3CountryCode.isNullOrBlank()) null else Locale.getAvailableLocales().find { it.iso3CountryOrNull == alpha3CountryCode }

/**
 * Return the ISO alpha-3 country code for the given ISO alpha-2 country code, or null if not available.
 */
fun alpha2ToAlpha3CountryCode(alpha2CountryCode: String?): String? =
    localeFromAlpha2CountryCode(alpha2CountryCode)?.iso3CountryOrNull

/**
 * Return the ISO alpha-2 country code for the given ISO alpha-3 country code, or null if not available.
 */
fun alpha3ToAlpha2CountryCode(alpha3CountryCode: String?): String? =
    localeFromAlpha3CountryCode(alpha3CountryCode)?.country

/**
 * Parse a country code from an unknown format.
 *
 * Trim the input, convert to upper case, then try
 *
 *  - ISO alpha-3
 *  - ISO alpha-2
 *  - single-letter licence plate numbers of European countries.
 *
 * Returns an ISO alpha-3 code, or null if nothing matched.
 *
 * [alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2),
 * [alpha-3](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3),
 * [Vehicle registration plates of Europe](https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Europe#European_Union)
 */
fun parseToAlpha3CountryCode(countryCode: String?): String? =
    countryCode?.trim()?.toUpperCase()?.let {
        when {
            Regex("[A-Z]{3}").matches(it) -> localeFromAlpha3CountryCode(it)?.iso3CountryOrNull
            Regex("[A-Z]{2}").matches(it) -> localeFromAlpha2CountryCode(it)?.iso3CountryOrNull
            it === "A" -> "AUT"
            it === "B" -> "BEL"
            it === "D" -> "DEU"
            it === "E" -> "ESP"
            it === "F" -> "FRA"
            it === "H" -> "HUN"
            it === "I" -> "ITA"
            it === "L" -> "LUX"
            it === "M" -> "MLT"
            it === "N" -> "NOR"
            it === "P" -> "PRT"
            it === "S" -> "SWE"
            it === "V" -> "VAT"
            else -> null
        }
    }

/**
 * Returns the display name of the country for the given alpha3 country code in the language of the given locale.
 */
fun countryDisplayName(alpha3CountryCode: String?, locale: Locale): String? =
    localeFromAlpha3CountryCode(alpha3CountryCode)?.getDisplayCountry(locale)

/**
 * Convenience wrapper for [MessageSource.message] accepting vararg arguments.
 */
fun MessageSource.message(locale: Locale, code: String, vararg args: Any): String =
    getMessage(code, args, locale)

/**
 * Convenience method to get a [DateTimeFormatter] from a format from a [MessageSource].
 */
fun MessageSource.formatter(locale: Locale, code: String, vararg args: Any): DateTimeFormatter =
    DateTimeFormatter.ofPattern(message(locale, code, args)).withLocale(locale)
