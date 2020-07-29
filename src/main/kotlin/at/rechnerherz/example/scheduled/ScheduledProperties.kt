package at.rechnerherz.example.scheduled

import at.rechnerherz.example.config.validation.ValidCronPattern
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "at.rechnerherz.example.base.scheduled")
class ScheduledProperties {

    /** A cron string that determines when to run watchdog services. */
    @ValidCronPattern
    var watchdogCron: String = Scheduled.CRON_DISABLED

    var pendingBookingMailDelay: Long = -1
}
