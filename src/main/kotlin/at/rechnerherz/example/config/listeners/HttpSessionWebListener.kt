package at.rechnerherz.example.config.listeners

import mu.KotlinLogging
import java.time.Duration
import java.time.Instant
import javax.servlet.annotation.WebListener
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

/**
 * An HttpSessionListener [WebListener] to log session creation and destruction.
 */
@WebListener
class HttpSessionWebListener : HttpSessionListener {

    private val log = KotlinLogging.logger {}

    override fun sessionCreated(event: HttpSessionEvent) {
        log.trace {
            val creationTime = Instant.ofEpochMilli(event.session.creationTime)
            "HttpSession created: ${event.session.id} (creationTime: $creationTime)"
        }
    }

    override fun sessionDestroyed(event: HttpSessionEvent) {
        log.trace {
            val creationTime = Instant.ofEpochMilli(event.session.creationTime)
            val lastAccessedTime = Instant.ofEpochMilli(event.session.lastAccessedTime)
            val duration = Duration.between(creationTime, lastAccessedTime).toMillis()
            "HttpSession destroyed: ${event.session.id} after $duration ms (creationTime: $creationTime, lastAccessedTime: $lastAccessedTime)"
        }
    }
}
