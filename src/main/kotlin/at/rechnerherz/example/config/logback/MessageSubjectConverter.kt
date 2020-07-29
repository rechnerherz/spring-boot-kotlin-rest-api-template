package at.rechnerherz.example.config.logback

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

/**
 * A [ClassicConverter] that returns the first line of the message or the name of the throwable.
 */
class MessageSubjectConverter : ClassicConverter() {

    override fun convert(event: ILoggingEvent): String {
        val firstLine = event.formattedMessage.substringBefore("\n").trim()
        return if ((firstLine.isEmpty() || firstLine == "null") && event.throwableProxy != null)
            event.throwableProxy.className
        else
            firstLine
    }

}
