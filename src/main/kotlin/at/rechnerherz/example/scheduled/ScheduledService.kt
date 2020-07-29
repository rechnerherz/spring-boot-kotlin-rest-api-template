package at.rechnerherz.example.scheduled

import at.rechnerherz.example.web.SitemapService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant

/**
 * Runs regularly and calls all other daily services.
 */
@Service
class ScheduledService(
    private val siteMapService: SitemapService
) : ApplicationRunner {

    /**
     * Run on startup.
     */
    override fun run(args: ApplicationArguments?) {
        val now = Instant.now()
        siteMapService.generateSitemapAndRobotsTxt(now)
    }

    /**
     * Run regularly.
     */
    @Scheduled(cron = "\${at.rechnerherz.example.base.scheduled.watchdog-cron}")
    fun dailyWatchdog() {
        val now = Instant.now()
        siteMapService.generateSitemapAndRobotsTxt(now)
    }

}

