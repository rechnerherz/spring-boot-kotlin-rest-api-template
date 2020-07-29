package at.rechnerherz.example.external.aws

import at.rechnerherz.example.external.aws.S3Config.Companion.S3_PROTOCOL_PREFIX
import at.rechnerherz.example.util.appendPath
import at.rechnerherz.example.util.readToString
import at.rechnerherz.example.util.writeString
import com.amazonaws.services.s3.AmazonS3
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.WritableResource
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Service to test the connection to AWS S3 on startup.
 */
@Service
@Order(-2)
class S3TestService(
    private val amazonS3: AmazonS3,
    private val s3Properties: S3Properties,
    private val resourceLoader: ResourceLoader
) : ApplicationRunner {

    private val log = KotlinLogging.logger {}

    private val bucketName = s3Properties.bucketName

    override fun run(args: ApplicationArguments?) {
        if (s3Properties.testOnStartup) {
            try {
                testOnStartup()
            } catch (e: Exception) {
                log.error(e) { "Could not connect to S3 bucket: $bucketName" }
            }
        }
    }

    private fun testOnStartup() {
        val region = amazonS3.regionName

        log.info { "S3 client using region: $region" }

        if (amazonS3.doesBucketExistV2(bucketName))
            log.info { "S3 bucket exists: $bucketName" }
        else
            log.error { "S3 bucket doesn't exist: $bucketName" }

        testWriteAndReadResource()
    }

    private fun testWriteAndReadResource() {
        val location = "$S3_PROTOCOL_PREFIX$bucketName".appendPath("test.txt")
        val content = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

        log.debug { "Write to $location: $content" }
        val writeResource = resourceLoader.getResource(location) as WritableResource
        writeResource.outputStream.writeString(content)

        val readResource = resourceLoader.getResource(location)
        val result = readResource.inputStream.readToString()
        log.debug { "Read from $location: $result" }

        check(content == result) {
            "Failed to read or write from S3."
        }
    }
}
