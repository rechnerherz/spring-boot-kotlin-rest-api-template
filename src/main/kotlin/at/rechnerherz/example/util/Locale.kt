package at.rechnerherz.example.util

import org.springframework.context.MessageSource
import java.util.*

fun localeFromAlpha2CountryCode(alpha2CountryCode: String): Locale? =
    Locale.getAvailableLocales().find { it.country == alpha2CountryCode }

fun localeFromAlpha3CountryCode(alpha3CountryCode: String): Locale? =
    Locale.getAvailableLocales().find {
        try {
            it.isO3Country == alpha3CountryCode
        } catch (e: MissingResourceException) {
            false
        }
    }

fun alpha2ToAlpha3CountryCode(alpha2CountryCode: String): String? =
    localeFromAlpha2CountryCode(alpha2CountryCode)?.isO3Country

fun alpha3ToAlpha2CountryCode(alpha3CountryCode: String): String? =
    localeFromAlpha3CountryCode(alpha3CountryCode)?.country

/**
 * Parse a country code from an unknown format.
 *
 * Trim the input then try
 *  - ISO alpha-3
 *  - ISO alpha-2
 *  - single-letter licence plate numbers of European countries.
 * Returns an ISO alpha-3 code, or null if nothing matched.
 *
 * [alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2),
 * [alpha-3](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3),
 * [Vehicle registration plates of Europe](https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Europe#European_Union)
 */
fun parseToAlpha3CountryCode(countryCode: String?): String? =
    countryCode?.trim()?.let {
        when {
            Regex("[A-Z]{3}").matches(it) -> localeFromAlpha2CountryCode(it)?.isO3Country
            Regex("[A-Z]{2}").matches(it) -> alpha2ToAlpha3CountryCode(it)
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
fun countryDisplayName(alpha3CountryCode: String, locale: Locale): String? =
    localeFromAlpha3CountryCode(alpha3CountryCode)?.getDisplayCountry(locale)

/**
 * Convenience wrapper for [MessageSource.getMessage] accepting vararg arguments.
 */
fun MessageSource.getMessage(locale: Locale, code: String, vararg args: Any): String =
    getMessage(code, args, locale)
