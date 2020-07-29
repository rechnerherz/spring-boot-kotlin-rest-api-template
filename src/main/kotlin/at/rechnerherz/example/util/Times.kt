package at.rechnerherz.example.util

import java.time.*
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ChronoZonedDateTime
import java.time.temporal.TemporalAmount
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

fun LocalDate.atEndOfDay(): LocalDateTime =
    LocalDateTime.of(this, LocalTime.MAX)

fun ZonedDateTime.toDateAtStartOfDay(): ZonedDateTime =
    toLocalDate().atStartOfDay(this.zone)

// Comparison functions

fun ChronoLocalDateTime<*>.isBeforeOrEqual(other: ChronoLocalDateTime<*>): Boolean =
    !isAfter(other)

fun ChronoLocalDateTime<*>.isAfterOrEqual(other: ChronoLocalDateTime<*>): Boolean =
    !isBefore(other)

fun ChronoZonedDateTime<*>.isBeforeOrEqual(other: ChronoZonedDateTime<*>): Boolean =
    !isAfter(other)

fun ChronoZonedDateTime<*>.isAfterOrEqual(other: ChronoZonedDateTime<*>): Boolean =
    !isBefore(other)

fun ZonedDateTime.isPast(): Boolean =
    isBefore(ZonedDateTime.now(zone))

fun ZonedDateTime.isFuture(): Boolean =
    isAfter(ZonedDateTime.now(zone))

fun ZonedDateTime.isPastOrNow(): Boolean =
    isBeforeOrEqual(ZonedDateTime.now(zone))

fun ZonedDateTime.isFutureOrNow(): Boolean =
    isAfterOrEqual(ZonedDateTime.now(zone))

fun ZonedDateTime.isEqualDayOfYear(other: ZonedDateTime?): Boolean =
    other == null || dayOfYear == other.dayOfYear

// Convert numbers to Duration/Period

val Int.nanos: Duration
    get() = Duration.ofNanos(toLong())

val Int.millis: Duration
    get() = Duration.ofMillis(toLong())

val Int.seconds: Duration
    get() = Duration.ofSeconds(toLong())

val Int.minutes: Duration
    get() = Duration.ofMinutes(toLong())

val Int.hours: Duration
    get() = Duration.ofHours(toLong())

val Int.days: Period
    get() = Period.ofDays(this)

val Int.weeks: Period
    get() = Period.ofWeeks(this)

val Int.months: Period
    get() = Period.ofMonths(this)

val Int.years: Period
    get() = Period.ofYears(this)

// Create temporals before or after another temporal

infix fun TemporalAmount.before(instant: Instant): Instant =
    instant.minus(this)

infix fun TemporalAmount.after(instant: Instant): Instant =
    instant.plus(this)

infix fun TemporalAmount.before(zonedDateTime: ZonedDateTime): ZonedDateTime =
    zonedDateTime.minus(this)

infix fun TemporalAmount.after(zonedDateTime: ZonedDateTime): ZonedDateTime =
    zonedDateTime.plus(this)

infix fun TemporalAmount.before(localDateTime: LocalDateTime): LocalDateTime =
    localDateTime.minus(this)

infix fun TemporalAmount.after(localDateTime: LocalDateTime): LocalDateTime =
    localDateTime.plus(this)

infix fun TemporalAmount.before(localDate: LocalDate): LocalDate =
    localDate.minus(this)

infix fun TemporalAmount.after(localDate: LocalDate): LocalDate =
    localDate.plus(this)

// Convert instants

fun Instant.toLocalDate(): LocalDate =
    atZone(TimeZone.getDefault().toZoneId()).toLocalDate()

fun Instant.toLocalDateTime(): LocalDateTime =
    atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()

// XMLGregorianCalendar

fun ZonedDateTime.toXMLGregorianCalendar(): XMLGregorianCalendar =
    DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(this))

fun LocalDate.toXMLGregorianCalendar(): XMLGregorianCalendar =
    DatatypeFactory.newInstance().newXMLGregorianCalendar(this.toString())
