package at.rechnerherz.example.config

import org.apache.tomcat.util.http.Rfc6265CookieProcessor
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TomcatConfig {

    /**
     * Set `SameSite=none` on session cookie (`JSESSIONID`).
     *
     * [Configure SameSite attribute on session Cookies](https://github.com/spring-projects/spring-boot/issues/15047)
     */
    @Bean
    fun cookieProcessorCustomizer(): WebServerFactoryCustomizer<TomcatServletWebServerFactory> =
        WebServerFactoryCustomizer { tomcatServletWebServerFactory ->
            tomcatServletWebServerFactory.addContextCustomizers(TomcatContextCustomizer { context ->
                context.cookieProcessor = Rfc6265CookieProcessor().apply {
                    setSameSiteCookies("none")
                }
            })
        }
}
