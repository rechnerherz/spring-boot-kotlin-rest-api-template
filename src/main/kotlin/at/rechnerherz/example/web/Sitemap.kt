package at.rechnerherz.example.web

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

const val SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9"

/**
 * Jackson XML mapping to generate a sitemap.
 *
 * [Sitemaps XML format](https://www.sitemaps.org/protocol.html)
 */
@JacksonXmlRootElement(localName = "urlset", namespace = SITEMAP_NAMESPACE)
data class Sitemap(

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "url", namespace = SITEMAP_NAMESPACE)
    val url: List<URL>

) {

    data class URL(

        @JacksonXmlProperty(localName = "loc", namespace = SITEMAP_NAMESPACE)
        val location: String,

        @JacksonXmlProperty(localName = "lastmod", namespace = SITEMAP_NAMESPACE)
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonFormat(pattern = "yyyy-MM-dd")
        val lastModified: LocalDate,

        @JacksonXmlProperty(localName = "changefreq", namespace = SITEMAP_NAMESPACE)
        val changeFrequency: ChangeFrequency,

        @JacksonXmlProperty(localName = "priority", namespace = SITEMAP_NAMESPACE)
        val priority: Float
    )
}

enum class ChangeFrequency {
    @JsonProperty("always")
    ALWAYS,
    @JsonProperty("hourly")
    HOURLY,
    @JsonProperty("daily")
    DAILY,
    @JsonProperty("weekly")
    WEEKLY,
    @JsonProperty("monthly")
    MONTHLY,
    @JsonProperty("yearly")
    YEARLY,
    @JsonProperty("never")
    NEVER
}
