package at.rechnerherz.example.util

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

/**
 * Divides the value by 100. Returns a correctly scaled BigDecimal.
 */
fun Long.percent(): BigDecimal =
    BigDecimal.valueOf(this).scaleByPowerOfTen(-2)

/**
 * Divides the value by 100. Returns a correctly scaled BigDecimal.
 */
fun Int.percent(): BigDecimal =
    BigDecimal.valueOf(toLong()).scaleByPowerOfTen(-2)

/**
 * Divides the value by 100. Returns a correctly scaled BigDecimal.
 */
fun BigDecimal.percent(): BigDecimal =
    scaleByPowerOfTen(-2)

/**
 * Multiplies the value by 100 and rounds (half-even) to an Int.
 */
fun BigDecimal.roundToIntPercent(): Int =
    scaleByPowerOfTen(2).round(MathContext(0, RoundingMode.HALF_EVEN)).toInt()

/**
 * Parse a BigDecimal from a number string formatted in the given locale.
 *
 * e.g. `parseBigDecimal("23.000,00", Locale.GERMAN)`
 */
fun parseBigDecimal(numberString: String, locale: Locale): BigDecimal =
    try {
        val decimalFormat = NumberFormat.getInstance(locale) as DecimalFormat
        decimalFormat.isParseBigDecimal = true
        decimalFormat.parseObject(numberString) as BigDecimal
    } catch (e: ParseException) {
        throw NumberFormatException("Could not parse BigDecimal from $numberString in locale $locale")
    }
