package at.rechnerherz.example.config.web

import mu.KotlinLogging
import org.springframework.core.io.Resource
import org.springframework.web.servlet.resource.PathResourceResolver
import java.io.File

/**
 * A [PathResourceResolver] that first tries to resolve the resource from its path,
 * and then tries again to resolve the original resource without any image size suffix.
 */
class ImagePathResourceResolver(
    private val extraImageSizeSuffices: List<String>
) : PathResourceResolver() {

    private val log = KotlinLogging.logger {}

    override fun getResource(resourcePath: String, location: Resource): Resource? {
        val resource = super.getResource(resourcePath, location)
        if (resource != null)
            return resource

        val file = File(resourcePath)
        val basename = file.nameWithoutExtension
        val extension = file.extension

        val endsWithSuffix = extraImageSizeSuffices.find { basename.endsWith(it) }
        return if (endsWithSuffix != null) {
            val originalFilename = "${basename.removeSuffix(endsWithSuffix)}.$extension"
            log.debug { "Fallback to resolving resource $originalFilename under $location" }
            super.getResource(originalFilename, location)
        } else
            null
    }

}
