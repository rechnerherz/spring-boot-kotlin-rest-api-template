package at.rechnerherz.example.external.aws

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

/**
 * S3 properties.
 */
@ConfigurationProperties(prefix = "at.rechnerherz.example.aws.s3")
@Validated
class S3Properties {

    /** Region name. */
    var regionName: String = "eu-central-1"

    /** S3 bucket name. */
    var bucketName: String = ""

    /** Whether to do a test read/write on S3 on startup. */
    var testOnStartup: Boolean = false
}
