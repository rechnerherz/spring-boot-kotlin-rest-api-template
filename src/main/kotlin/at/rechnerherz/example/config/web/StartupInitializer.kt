package at.rechnerherz.example.config.web

import com.twelvemonkeys.servlet.image.IIOProviderContextListener
import mu.KotlinLogging
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.ServletContext

@Component
class StartupInitializer : ServletContextInitializer {

    private val log = KotlinLogging.logger {}

    override fun onStartup(servletContext: ServletContext) {

        val originalTimeZone = TimeZone.getDefault().id
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        val defaultTimeZone = TimeZone.getDefault().id

        log.info { "Set default JVM time zone to $defaultTimeZone (was $originalTimeZone)" }

        val originalLocale = Locale.getDefault().toLanguageTag()
        Locale.setDefault(Locale.ENGLISH)
        val defaultLocale = Locale.getDefault().toLanguageTag()

        log.info { "Set default JVM locale to $defaultLocale (was $originalLocale)" }

        // Add TwelveMonkeys IIOProviderContextListener to register and de-register ImageIO plugins
        // https://github.com/haraldk/TwelveMonkeys/issues/16
        servletContext.addListener(IIOProviderContextListener::class.java)

    }
}
