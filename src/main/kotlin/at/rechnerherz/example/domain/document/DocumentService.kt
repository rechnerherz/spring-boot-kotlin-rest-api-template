package at.rechnerherz.example.domain.document

import at.rechnerherz.example.config.DOCUMENTS_DIRECTORY
import at.rechnerherz.example.config.aop.NoProfiling
import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.util.appendPath
import at.rechnerherz.example.util.copyToResource
import com.github.slugify.Slugify
import mu.KotlinLogging
import net.coobird.thumbnailator.Thumbnails
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.WritableResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Files
import java.util.*
import javax.imageio.ImageIO

@Service
@Order(-1)
class DocumentService(
    private val baseProperties: BaseProperties,
    private val documentProperties: DocumentProperties,
    private val resourceLoader: ResourceLoader
) : ApplicationRunner {

    private val log = KotlinLogging.logger {}

    private val slugify: Slugify = Slugify().withLowerCase(false)

    /**
     * Create directories on startup (if the [BaseProperties.directory] is a file resource).
     */
    @Throws(IOException::class)
    @NoProfiling
    override fun run(args: ApplicationArguments?) {
        val baseDirectory = resourceLoader.getResource(baseProperties.directory)
        if (baseDirectory.isFile) {
            val documentsDirectory = baseDirectory.file.toPath().resolve(DOCUMENTS_DIRECTORY)
            log.debug { "Creating documents directory: $documentsDirectory" }
            Files.createDirectories(documentsDirectory)
        } else {
            log.debug { "Using documents directory: $baseDirectory" }
        }
    }

    /**
     * Access a document from the file system as a resource.
     */
    fun documentAsResource(document: Document): Resource =
        documentAsResource(document.fileName)

    /**
     * Access a document from the file system as a resource.
     */
    fun documentAsResource(fileName: String): Resource =
        resourceLoader.getResource(baseProperties.directory.appendPath(DOCUMENTS_DIRECTORY).appendPath(fileName))

    /**
     * Slugified original filename of the [MultipartFile] without extension.
     */
    private fun MultipartFile.slugifiedName(): String =
        slugify.slugify((originalFilename ?: "").substringBeforeLast("."))

    /**
     * Save a [MultipartFile] to the filesystem.
     *
     * If the file is an image shrink it if it is too large and create thumbnails.
     */
    @Throws(IOException::class)
    fun saveMultipartFile(multipartFile: MultipartFile): Document =
        saveResource(
            multipartFile.resource,
            DocumentType.fromContentType(multipartFile.contentType),
            multipartFile.slugifiedName()
        )

    /**
     * Save a [Resource] to the filesystem or to S3.
     *
     * If the source is an image shrink it and create thumbnails.
     */
    @Throws(IOException::class)
    fun saveResource(
        source: Resource,
        type: DocumentType,
        name: String
    ): Document {
        val uuid = UUID.randomUUID()
        val basename = uuid.toString()
        val extension = type.extension()

        return if (type.isImage()) {
            val size = processImage(type, source, basename, extension)
            Document(uuid, type, name).apply {
                this.width = size.width
                this.height = size.height
            }
        } else {
            copyResource(source, basename, extension)
            Document(uuid, type, name)
        }
    }

    /**
     * Shrink the image if it is too large (overwriting the original file) and create thumbnails.
     */
    @Throws(IOException::class)
    private fun processImage(
        type: DocumentType,
        source: Resource,
        basename: String,
        extension: String
    ): Dimension {

        val image = source.inputStream.use {
            ImageIO.read(it) ?: throw IOException("No registered ImageReader was able to read $source")
        }

        val destination = documentAsResource(DOCUMENTS_DIRECTORY.appendPath("$basename.$extension")) as WritableResource

        if (!shrinkImage(type, source, image, destination, documentProperties.maxImageSize)) {
            log.debug { "Copying $source to $destination" }
            source.copyToResource(destination)
        }

        documentProperties.extraImageSizes.forEach { (suffix, size) ->
            val thumbnailDestination =
                documentAsResource(DOCUMENTS_DIRECTORY.appendPath("$basename.$suffix.$extension")) as WritableResource
            shrinkImage(type, source, image, thumbnailDestination, size)
        }

        return Dimension(image.width, image.height)
    }

    /**
     * If the image is larger than [maxSize] in either dimension, shrink it to fit in a [maxSize] by [maxSize] square (keeping the aspect ratio).
     */
    @Throws(IOException::class)
    private fun shrinkImage(
        type: DocumentType,
        source: Resource,
        sourceImage: BufferedImage,
        destination: WritableResource,
        maxSize: Int
    ): Boolean {
        return if (sourceImage.height > maxSize || sourceImage.width > maxSize) {
            log.debug { "Resizing image $source to $destination size $maxSize" }
            destination.outputStream.use { output ->
                Thumbnails.of(sourceImage)
                    .size(maxSize, maxSize)
                    .outputFormat(type.name)
                    .toOutputStream(output)
            }
            true
        } else {
            false
        }
    }

    /**
     * Copy the source to the destination.
     */
    @Throws(IOException::class)
    private fun copyResource(
        source: Resource,
        basename: String,
        extension: String
    ) {
        val destination = documentAsResource("$basename.$extension") as WritableResource
        log.debug { "Copying $source to $destination" }
        source.copyToResource(destination)
    }

}
