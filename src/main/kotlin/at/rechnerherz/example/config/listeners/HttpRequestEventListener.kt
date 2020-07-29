package at.rechnerherz.example.config.listeners

import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.context.support.RequestHandledEvent

/**
 * An [EventListener] for logging handled HTTP requests.
 */
@Component
class HttpRequestEventListener {

    private val log = KotlinLogging.logger {}

    @EventListener
    fun handle(event: RequestHandledEvent) {
        log.trace { "HTTP request handled: ${event.description}" }
    }
}
