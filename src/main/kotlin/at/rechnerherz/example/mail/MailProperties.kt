package at.rechnerherz.example.mail

import at.rechnerherz.example.util.millis
import org.hibernate.validator.constraints.time.DurationMin
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.time.Duration
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

@ConfigurationProperties(prefix = "at.rechnerherz.example.mail")
@Validated
class MailProperties {

    /** Enable sending mails with the MailService. */
    var enabled: Boolean = true

    /** Include the mail body in the log. */
    var logBody: Boolean = false

    /** Send all mails to the debug address instead of the real receivers. */
    var debugMode: Boolean = false

    /** Send all mails to the debug address as BCC. */
    var debugBcc: Boolean = false

    /** An email address used for debugging. */
    @NotBlank
    @Email
    var debugAddress: String = ""

    /** Personal identifier for debug email address. */
    var debugPersonal: String = ""

    /** An email address used for sending notifications. */
    @NotBlank
    @Email
    var fromAddress: String = ""

    /** Personal identifier for from email address. */
    var fromPersonal: String = ""

    /** Time to wait between sending mails. */
    @DurationMin
    var sendDelay: Duration = 100.millis

    /** Time to wait before retrying after a connection error. */
    @DurationMin
    var retryDelay: Duration = 1000.millis

    /** Maximum number of retries for connection errors. */
    @PositiveOrZero
    var maxRetries: Int = 3
}
