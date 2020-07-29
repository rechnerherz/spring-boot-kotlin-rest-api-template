package at.rechnerherz.example.external.aws

import mu.KotlinLogging
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

@Component
class FileActionHandler : ResourceActionHandler {

    private val log = KotlinLogging.logger {}

    override fun accepts(resource: Resource): Boolean =
        resource.isFile

    override fun moveFile(source: Path, target: Resource) {
        require(accepts(target)) {
            "Cannot handle $target"
        }

        val directory = target.file.toPath()
        val targetFile = directory.resolve(source.fileName)

        log.debug("Moving $source to $targetFile")

        Files.createDirectories(directory)
        Files.move(source, targetFile)
    }

    override fun deleteFiles(resource: Resource, excludedFiles: Set<String>) {
        require(accepts(resource)) {
            "Cannot handle $resource"
        }

        val directory = resource.file.toPath()

        if (!Files.isDirectory(directory)) {
            log.warn { "$directory doesn't exist or is not a directory" }
            return
        }

        log.debug { "Deleting files in $directory (excluded ${excludedFiles.size})" }

        directory.toFile().walkTopDown().forEach { file ->
            if (file.isFile) {
                if (excludedFiles.find { file.endsWith(it) } == null) {
                    log.trace { "Deleting $file" }
                    Files.delete(file.toPath())
                } else {
                    log.trace { "Keeping  $file" }
                }
            }
        }
    }

    override fun deleteOldFiles(resource: Resource, olderThan: Instant) {
        require(accepts(resource)) {
            "Cannot handle $resource"
        }

        val directory = resource.file.toPath()

        if (!Files.isDirectory(directory)) {
            log.warn { "$directory doesn't exist or is not a directory" }
            return
        }

        log.debug { "Deleting files in $directory older than $olderThan" }

        directory.toFile().walkTopDown().forEach { file ->
            if (file.isFile) {
                val lastModified = Files.getLastModifiedTime(file.toPath()).toInstant()
                if (lastModified.isBefore(olderThan)) {
                    log.trace { "Deleting $file (last modified: $lastModified)" }
                    Files.delete(file.toPath())
                } else {
                    log.trace { "Keeping  $file (last modified: $lastModified)" }
                }
            }
        }

        // delete empty directories
        directory.toFile().walkTopDown().forEach { file ->
            if (file.isDirectory && file.listFiles()?.isEmpty() == true) {
                Files.delete(file.toPath())
            }
        }
    }
}
