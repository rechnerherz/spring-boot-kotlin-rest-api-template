package at.rechnerherz.example.config.logback

import ch.qos.logback.classic.Level
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@ConfigurationProperties(prefix = "at.rechnerherz.example.logback")
@Validated
class LogbackProperties {

    /** Nested logback.mail properties. */
    @Valid
    val mail: Mail = Mail()

    /** Nested logback.slack properties. */
    @Valid
    val slack: Slack = Slack()

    /** Nested logback.sentry properties. */
    @Valid
    val sentry: Sentry = Sentry()

    class Mail {

        /** Enable sending mails via logback. */
        var enabled: Boolean = false

        /** Send mails for log statements of this level or higher. */
        var level: Level = Level.ERROR

        /** The sender mail address to use for logback mails. */
        @Email
        @NotBlank
        var fromAddress: String = ""

        var fromPersonal: String = ""

        /** The receiver mail address to use for logback mails. */
        @Email
        @NotBlank
        var toAddress: String = ""

        var toPersonal: String = ""

        /** A comma-separated list of loggers to ignore when sending mails. */
        @Pattern(regexp = """|(\S+(,\S+)*)""")
        var ignore: String = ""

        /** A comma-separated list of loggers to send mails for log statements of any level. */
        @Pattern(regexp = """|(\S+(,\S+)*)""")
        var enforce: String = ""
    }

    class Slack {

        /** Enable posting to Slack via logback. */
        var enabled: Boolean = false

        /** The Slack incoming webhook URL. */
        @Pattern(regexp = """https://hooks\.slack\.com/services/T[A-Z0-9]{8}/B[A-Z0-9]{8}/[a-zA-Z0-9]{24}""")
        var webhook: String = ""

        /** The Slack channel to post to. */
        @Pattern(regexp = "|[#@][a-z]+")
        var channel: String? = "#general"

        /** The username to use for the Slack message. If not set, the hostname will be used instead. */
        var username: String = ""

        /** The icon to use for the Slack message (optional). */
        @Pattern(regexp = "|:[a-z0-9_-]+:")
        var icon: String = ""
    }

    class Sentry {

        /** Enable sending sentry events via logback. */
        var enabled: Boolean = false

        /** Send sentry events for log statements of this level or higher. */
        var level: Level = Level.WARN

        /** A comma-separated list of loggers to ignore when sending sentry events. */
        @Pattern(regexp = """|(\S+(,\S+)*)""")
        var ignore: String = ""

        /** A comma-separated list of loggers to send sentry events for log statements of any level. */
        @Pattern(regexp = """|(\S+(,\S+)*)""")
        var enforce: String = ""
    }
}
