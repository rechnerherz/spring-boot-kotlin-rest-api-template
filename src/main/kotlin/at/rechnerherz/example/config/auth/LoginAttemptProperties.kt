package at.rechnerherz.example.config.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.util.concurrent.TimeUnit
import javax.validation.constraints.Positive

@ConfigurationProperties(prefix = "at.rechnerherz.example.login")
@Validated
class LoginAttemptProperties {

    /** The expiration time for failed login attempts. */
    @Positive
    var expiry: Long = 1

    /** The expiration time unit for failed login attempts. */
    var expiryUnit: TimeUnit = TimeUnit.DAYS

    /** Number of failed login attempts per IP to start notifying. */
    @Positive
    var ipWarn = 10

    /** Max number of failed login attempts before the IP is blocked. */
    @Positive
    var ipBlock = 11

    /** Number of failed login attempts per IP and username to start notifying. */
    @Positive
    var usernameWarn = 10

    /** Max number of failed login attempts before the IP is blocked. */
    @Positive
    var usernameBlock = 11
}
