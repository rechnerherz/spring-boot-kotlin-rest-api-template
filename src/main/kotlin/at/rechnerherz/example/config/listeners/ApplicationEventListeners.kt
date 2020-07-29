package at.rechnerherz.example.config.listeners

import at.rechnerherz.example.config.logback.SEND_MAIL
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationFailedEvent
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.info.BuildProperties
import org.springframework.context.event.*
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
 * [EventListener]s for logging application context events and application events provided by Spring Boot.
 *
 * Sends mails for [ApplicationReadyEvent] and [ApplicationFailedEvent], using build info from [BuildProperties].
 *
 * [Application Events and Listeners](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-application-events-and-listeners),
 * [Standard and Custom Events](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-functionality-events),
 * [Generate Build Information](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html#howto-build-info)
 */
@Component
class ApplicationEventListeners(
    buildProperties: BuildProperties,
    environment: Environment
) {

    private val log = KotlinLogging.logger {}

    private val name = buildProperties.name
    private val version = buildProperties.version
    private val profile = environment.activeProfiles[0]

    @EventListener
    fun handle(event: ApplicationStartedEvent) {

        // set system properties for sentry
        System.setProperty("sentry.environment", profile)
        System.setProperty("sentry.release", version)

        log.trace("Application started: $name:$version")
    }

    @EventListener
    fun handle(event: ApplicationReadyEvent) {
        log.info(SEND_MAIL, "Application ready: $name:$version")
    }

    @EventListener
    fun handle(event: ApplicationFailedEvent) {
        log.warn(SEND_MAIL, "Application failed: $name:$version")
    }

    @EventListener
    fun handle(event: ContextStartedEvent) {
        log.trace("Application context started")
    }

    @EventListener
    fun handle(event: ContextRefreshedEvent) {
        log.trace("Application context refreshed")
    }

    @EventListener
    fun handle(event: ContextStoppedEvent) {
        log.trace("Application context stopped")
    }

    @EventListener
    fun handle(event: ContextClosedEvent) {
        log.trace("Application context closed")
    }
}
