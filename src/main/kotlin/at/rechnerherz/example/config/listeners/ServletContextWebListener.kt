package at.rechnerherz.example.config.listeners

import mu.KotlinLogging
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

/**
 * ServletContextListener [WebListener] to log Servlet context initialization and destruction.
 */
@WebListener
class ServletContextWebListener : ServletContextListener {

    private val log = KotlinLogging.logger {}

    override fun contextInitialized(event: ServletContextEvent) {
        log.trace("Servlet context initialized")
    }

    override fun contextDestroyed(event: ServletContextEvent) {
        log.trace("Servlet context destroyed")
    }

}
