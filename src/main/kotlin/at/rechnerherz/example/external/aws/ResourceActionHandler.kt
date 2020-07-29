package at.rechnerherz.example.external.aws

import org.springframework.core.io.Resource
import java.nio.file.Path
import java.time.Instant

/**
 * Interface to handle actions on different kinds of resources (file system, S3).
 */
interface ResourceActionHandler {

    fun accepts(resource: Resource): Boolean

    fun moveFile(source: Path, target: Resource)

    fun deleteFiles(resource: Resource, excludedFiles: Set<String>)

    fun deleteOldFiles(resource: Resource, olderThan: Instant)
}

/**
 * Return a [ResourceActionHandler] that accepts the given [resource] or throw an exception.
 */
fun Iterable<ResourceActionHandler>.findAccepting(resource: Resource): ResourceActionHandler =
    find { it.accepts(resource) }
        ?: throw IllegalStateException("No ResourceActionHandler found to handle: $resource")
