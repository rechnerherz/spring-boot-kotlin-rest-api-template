package at.rechnerherz.example.config.logback

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

/**
 * A [ClassicConverter] that returns all the entries present in the MDC, one key=value pair per line.
 */
class MDCMultilineConverter : ClassicConverter() {

    override fun convert(event: ILoggingEvent): String =
        event.mdcPropertyMap.toSortedMap().entries.joinToString(separator = "\n")

}
