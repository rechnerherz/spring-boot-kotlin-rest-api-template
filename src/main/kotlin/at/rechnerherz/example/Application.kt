package at.rechnerherz.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Main application entry point.
 *
 * - [SpringBootApplication] enables auto-configuration and component scanning.
 *
 * - [ServletComponentScan] enables scanning for servlet components in embedded web servers.
 *
 * - [ConfigurationPropertiesScan] enables scanning for configuration property components.
 *
 * - [EnableAsync] and [EnableScheduling] for asynchronous and scheduled method execution.
 *
 * Excluded from auto-configuration:
 *
 * - ErrorMvcAutoConfiguration in favor of custom [at.rechnerherz.example.config.error.ExceptionHandling].
 *
 */
@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
@ServletComponentScan
@ConfigurationPropertiesScan
@EnableAsync
@EnableScheduling
class Application {

	companion object {

		/** Entry point, run with gradle `bootRun` task. */
		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(Application::class.java, *args)
		}

	}
}
