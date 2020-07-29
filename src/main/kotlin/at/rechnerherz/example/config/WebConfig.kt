package at.rechnerherz.example.config

import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.util.appendPath
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.resource.PathResourceResolver

/**
 * Spring Web configuration.
 */
@Configuration
class WebConfig(
    private val baseProperties: BaseProperties
) : WebMvcConfigurer {

    /**
     * Add resource handlers to serve static resources under the [STATIC_URL] (sitemap.xml, robots.txt).
     *
     * Note: Directory resource locations must end with '/'.
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
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

}
