package at.rechnerherz.example.external.aws

import at.rechnerherz.example.util.appendPath
import at.rechnerherz.example.util.copyToResource
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.S3ObjectSummary
import mu.KotlinLogging
import org.springframework.cloud.aws.core.io.s3.SimpleStorageResource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.WritableResource
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

@Component
class S3ActionHandler(
    private val amazonS3: AmazonS3,
    private val resourceLoader: ResourceLoader
) : ResourceActionHandler {

    private val log = KotlinLogging.logger {}

    override fun accepts(resource: Resource): Boolean =
        resource is SimpleStorageResource

    override fun moveFile(source: Path, target: Resource) {
        require(target is SimpleStorageResource) {
            "Cannot handle $target"
        }

        val bucketName = target.bucketName()
        val objectName = target.objectName()
        val targetLocation = "${S3Config.S3_PROTOCOL_PREFIX}$bucketName"
            .appendPath(objectName)
            .appendPath(source.fileName.toString())

        val targetResource = resourceLoader.getResource(targetLocation) as WritableResource

        log.debug("Moving $source to $targetResource")

        source.toFile().inputStream().copyToResource(targetResource)
        Files.delete(source)
    }

    /**
     * Delete all objects under [resource], except for ones matching [excludedFiles].
     *
     * [Delete a Bucket: Using the AWS SDKs](https://docs.aws.amazon.com/AmazonS3/latest/dev/delete-or-empty-bucket.html#delete-bucket-sdk-java)
     */
    override fun deleteFiles(resource: Resource, excludedFiles: Set<String>) {
        require(resource is SimpleStorageResource) {
            "Cannot handle $resource"
        }

        log.debug { "Deleting files in S3 bucket $resource (excluded ${excludedFiles.size})" }

        forEach(resource) { s3ObjectSummary ->
            if (excludedFiles.find { s3ObjectSummary.key.endsWith(it) } == null) {
                log.trace { "Deleting $s3ObjectSummary" }
                amazonS3.deleteObject(s3ObjectSummary.bucketName, s3ObjectSummary.key)
            } else {
                log.trace { "Keeping  $s3ObjectSummary" }
            }
        }
    }

    /**
     * Delete all objects under [resource] that have been last modified before [olderThan].
     */
    override fun deleteOldFiles(resource: Resource, olderThan: Instant) {
        require(resource is SimpleStorageResource) {
            "Cannot handle $resource"
        }

        log.debug { "Deleting files in S3 bucket $resource older than $olderThan)" }

        forEach(resource) { s3ObjectSummary ->
            val lastModified = s3ObjectSummary.lastModified.toInstant()
            if (lastModified.isBefore(olderThan)) {
                log.trace { "Deleting $s3ObjectSummary (last modified: $lastModified)" }
                amazonS3.deleteObject(s3ObjectSummary.bucketName, s3ObjectSummary.key)
            } else {
                log.trace { "Keeping  $s3ObjectSummary (last modified: $lastModified)" }
            }
        }
    }

    /**
     * Run [action] on all objects under [resource].
     */
    fun forEach(resource: Resource, action: (S3ObjectSummary) -> Unit) {
        require(resource is SimpleStorageResource) {
            "Cannot handle $resource"
        }

        val bucketName = resource.bucketName()
        val objectName = resource.objectName()

        var objectListing = amazonS3.listObjects(bucketName, objectName)
        while (true) {
            val iterator = objectListing.objectSummaries.iterator()
            while (iterator.hasNext()) {
                action(iterator.next())
            }

            // If the bucket contains many objects, the listObjects() call
            // might not return all of the objects in the first listing. Check to
            // see whether the listing was truncated.
            if (objectListing.isTruncated)
                objectListing = amazonS3.listNextBatchOfObjects(objectListing)
            else
                break

        }
    }
}
