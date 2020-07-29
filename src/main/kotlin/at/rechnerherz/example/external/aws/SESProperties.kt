package at.rechnerherz.example.external.aws

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

/**
 * SES properties.
 */
@ConfigurationProperties(prefix = "at.rechnerherz.example.aws.ses")
@Validated
class SESProperties {

    /** Region name. */
    var regionName: String = "eu-west-1"

    /** Whether to test connection on startup. */
    var testOnStartup: Boolean = false
}
