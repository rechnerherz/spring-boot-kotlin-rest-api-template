package at.rechnerherz.example.web

import at.rechnerherz.example.config.ROBOTS
import at.rechnerherz.example.config.SITEMAP
import at.rechnerherz.example.config.STATIC_DIRECTORY
import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.util.appendPath
import at.rechnerherz.example.util.toLocalDate
import at.rechnerherz.example.util.writeString
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.WritableResource
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.time.Instant

/**
 * Generates a sitemap daily, containing all bundle and static page URLs. Also generates a robots.txt.
 *
 * The files are stored under [BaseProperties.directory]/[STATIC_DIRECTORY].
 * They are resolved as static resources, see [at.rechnerherz.example.config.WebConfig.addResourceHandlers].
 */
@Service
class SitemapService(
    private val baseProperties: BaseProperties,
    private val resourceLoader: ResourceLoader
) {

    private val log = KotlinLogging.logger {}

    private val xmlMapper = Jackson2ObjectMapperBuilder()
        .modules(
            KotlinModule(),
            JavaTimeModule()
        )
        .createXmlMapper(true)
        .indentOutput(true)
        .build<XmlMapper>().apply {
            enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
        }

    private fun targetDirectory(): String =
        baseProperties.directory.appendPath(STATIC_DIRECTORY)

    @Transactional(readOnly = true)
    fun generateSitemapAndRobotsTxt(now: Instant) {
        try {
            val baseDirectory = resourceLoader.getResource(baseProperties.directory)
            if (baseDirectory.isFile)
                Files.createDirectories(resourceLoader.getResource(targetDirectory()).file.toPath())

            generateSitemap(now)
            generateRobotsTxt()
        } catch (e: Exception) {
            log.error(e) { "Failed to generate sitemap or robots.txt" }
        }
    }

    private fun generateSitemap(now: Instant) {
        val nowDate = now.toLocalDate()

        // Base URL
        val sitemapUrls = mutableListOf(
            Sitemap.URL(
                baseProperties.frontendUrl,
                nowDate,
                ChangeFrequency.DAILY,
                1.0f
            )
        )

        val sitemap = Sitemap(sitemapUrls)
        //println(xmlMapper.writeValueAsString(sitemap))
        val sitemapPath = targetDirectory().appendPath(SITEMAP)
        val sitemapResource = resourceLoader.getResource(sitemapPath) as WritableResource
        sitemapResource.outputStream.use { out ->
            xmlMapper.writeValue(out, sitemap)
        }
    }

    private fun generateRobotsTxt() {
        val robotsPath = targetDirectory().appendPath(ROBOTS)
        val robotsResource = resourceLoader.getResource(robotsPath) as WritableResource
        robotsResource.outputStream.use { out ->
            out.writeString(
                """
                |User-agent: *
                |Allow: /
                |Disallow: /backend/
                |Disallow: /tomcat/
                |Sitemap: ${baseProperties.frontendUrl.appendPath(SITEMAP)}
                """.trimMargin()
            )
        }
    }

}
