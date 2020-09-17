package at.rechnerherz.example.config

import at.rechnerherz.example.config.web.ImagePathResourceResolver
import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.domain.document.DocumentProperties
import at.rechnerherz.example.util.appendPath
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import org.springframework.web.accept.FixedContentNegotiationStrategy
import org.springframework.web.filter.ForwardedHeaderFilter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.resource.PathResourceResolver
import java.util.concurrent.TimeUnit

/**
 * Spring Web configuration.
 */
@Configuration
class WebConfig(
    private val baseProperties: BaseProperties,
    private val documentProperties: DocumentProperties
) : WebMvcConfigurer {

    /**
     * Always use "application/json" as media type
     * instead of the doing content negotiation based on the "Accept"-header or the path.
     */
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
            .strategies(listOf(FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON)))
    }

    /**
     * Add resource handlers to serve
     *
     * - documents from the public documents subdirectory as static resources under the [DOCUMENTS_URL]
     * - static resources under the [STATIC_URL] (sitemap.xml, robots.txt)
     *
     * Note: Directory resource locations must end with '/'.
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler(anySubPath(DOCUMENTS_URL))
            .addResourceLocations(baseProperties.directory.appendPath(DOCUMENTS_DIRECTORY))
            .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
            .resourceChain(true)
            .addResolver(ImagePathResourceResolver(
                documentProperties.extraImageSizes.keys.map { ".$it" }
            ))
        registry
            .addResourceHandler(anySubPath(STATIC_URL))
            .addResourceLocations(baseProperties.directory.appendPath(STATIC_DIRECTORY))
            .resourceChain(false)
            .addResolver(PathResourceResolver())
    }

    /**
     * The [CookieLocaleResolver] reads the user locale and timezone from a cookie.
     *
     * Fallback to default locale/timezone when the cookie contains in invalid value.
     *
     * [Locale](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-localeresolver)
     */
    @Bean
    fun localeResolver(): LocaleResolver =
        CookieLocaleResolver().apply {
            cookieName = baseProperties.localeCookieName
            cookieMaxAge = baseProperties.localeCookieMaxAge
            setDefaultLocale(baseProperties.defaultUserLocale())
            setDefaultTimeZone(baseProperties.defaultUserTimeZone())
            isRejectInvalidCookies = false
            isLanguageTagCompliant = true
        }

    /**
     * Register a [ForwardedHeaderFilter] to extract "Forwarded" and "X-Forwarded-*" headers,
     * necessary for running Tomcat behind a proxy.
     */
    @Bean
    fun forwardedHeaderFilter(): ForwardedHeaderFilter =
        ForwardedHeaderFilter()

    /**
     * Register a [MethodValidationPostProcessor] to do method-level validation
     * of parameters and return values.
     *
     * The target class must be annotated with [org.springframework.validation.annotation.Validated]
     * to be considered for validation.
     */
    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor =
        MethodValidationPostProcessor()
}
