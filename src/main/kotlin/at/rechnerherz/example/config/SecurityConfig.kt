package at.rechnerherz.example.config

import at.rechnerherz.example.config.auth.*
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.account.Role
import at.rechnerherz.example.domain.base.BaseProperties
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.HandlerExceptionResolver
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport

/**
 * Spring Security configuration.
 *
 * [Web Security Java Configuration](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#hello-web-security-java-configuration),
 * [Problem Spring Web Security integration](https://github.com/zalando/problem-spring-web#security)
 */
@Configuration
@Import(SecurityProblemSupport::class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val baseProperties: BaseProperties,
    private val accountRepository: AccountRepository,
    private val loginAttemptService: LoginAttemptService,
    private val securityProblemSupport: SecurityProblemSupport,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : WebSecurityConfigurerAdapter() {

    /**
     * Provide a [CorsConfigurationSource] to be used by the [org.springframework.web.filter.CorsFilter].
     *
     * Applies to all requests and allows all methods, headers and credentials,
     * but only allow requests from origins defined in [BaseProperties.correctedCorsAllowedOrigins].
     *
     * See [WebConfig] for CORS configuration for static documents.
     *
     * [CORS](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#cors)
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = baseProperties.correctedCorsAllowedOrigins()
        configuration.allowedMethods = listOf(CorsConfiguration.ALL)
        configuration.allowedHeaders = listOf(CorsConfiguration.ALL)
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration(ANY_PATH, configuration)
        return source
    }

    /**
     * Provide [AccountUserDetailsService] as the [UserDetailsService] implementation.
     */
    override fun userDetailsService(): UserDetailsService =
        AccountUserDetailsService(accountRepository)

    /**
     * Expose the [UserDetailsService] as a bean.
     *
     * This also disables the [org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration].
     */
    @Bean
    override fun userDetailsServiceBean(): UserDetailsService =
        super.userDetailsServiceBean()

    /**
     * Configure the [AuthenticationManagerBuilder] to build the [org.springframework.security.authentication.AuthenticationManager].
     *
     * Adds a [LoginAttemptDaoAuthenticationProvider] to handle login via username and password.
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        val userDetailsService = userDetailsService()
        val authenticationProvider = LoginAttemptDaoAuthenticationProvider(loginAttemptService, userDetailsService)
        auth
            .authenticationProvider(authenticationProvider)
            .userDetailsService(userDetailsService())
    }

    /**
     * Expose the [AuthenticationManager] as a bean.
     */
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()

    /**
     * WebSecurity configuration.
     *
     * Set debug to true to enable Spring Security debugging.
     */
    override fun configure(web: WebSecurity) {
//        web.debug(true)
    }

    /**
     * HttpSecurity configuration.
     *
     * [HttpSecurity](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#jc-httpsecurity)
     */
    override fun configure(http: HttpSecurity) {
        http
//            .disableHeaders()
            .requireSSL()
            .enableCors()
//            .enableStatelessCsrf()
            .authentication()
            .setRequestRestrictions()
            .exceptionHandlingProblemSupport()
    }

    /**
     * Disable security headers that we add with nginx to prevent duplicate headers.
     *
     * [Default Security Headers](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#default-security-headers)
     */
    private fun HttpSecurity.disableHeaders(): HttpSecurity =
        headers()
            .contentTypeOptions().disable()
            .httpStrictTransportSecurity().disable()
            .xssProtection().disable()
            .and()

    /**
     * Require HTTPS for all requests.
     */
    private fun HttpSecurity.requireSSL(): HttpSecurity =
        requiresChannel().anyRequest().requiresSecure().and()

    /**
     * Enable CORS support.
     *
     * Adds a [org.springframework.web.filter.CorsFilter] using the [CorsConfigurationSource] bean.
     */
    private fun HttpSecurity.enableCors(): HttpSecurity =
        cors().and()

    //TODO: use Spring's default CSRF protection?
    /**
     * Enable CSRF protection with a client-side generated double submit cookie.
     *
     * [Cross Site Request Forgery (CSRF)](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#csrf),
     */
    private fun HttpSecurity.enableStatelessCsrf(): HttpSecurity =
        csrf()
            .disable()
            .addFilterBefore(
                StatelessCsrfFilter(
                    securityProblemSupport,
                    AntPathRequestMatcher(CSP_REPORT_URL)
                ), CsrfFilter::class.java
            )

    /**
     * Register the [SecurityProblemSupport] for exception handling.
     *
     * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#auth-entry-point),
     * [AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#access-denied-handler)
     */
    private fun HttpSecurity.exceptionHandlingProblemSupport(): HttpSecurity =
        exceptionHandling()
            .authenticationEntryPoint(securityProblemSupport)
            .accessDeniedHandler(securityProblemSupport)
            .and()

    /**
     * Configure authentication.
     */
    private fun HttpSecurity.authentication(): HttpSecurity =
        formLogin()
            .loginProcessingUrl(LOGIN_URL)
            .successHandler { _, response, _ -> response.status = 200  }
            .failureHandler(AuthenticationFailureResolver(handlerExceptionResolver))
            .and()
            .logout()
            .logoutUrl(LOGOUT_URL)
            .logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler())
            .and()

    /**
     * Request restrictions:
     *
     * - GET / is public.
     * - /public paths are public.
     * - /api paths are restricted to fully authenticated accounts, and have further method security restrictions defined (@WithPermissionRule).
     * - /monitoring paths are restricted to admins.
     * - /actuator paths are restricted to admins.
     * - All other paths are forbidden.
     */
    private fun HttpSecurity.setRequestRestrictions(): HttpSecurity =
        authorizeRequests()
            .antMatchers(HttpMethod.GET, ROOT_URL, FAVICON, SITEMAP, ROBOTS).permitAll()
            .antMatchers(anySubPath(PUBLIC_URL)).permitAll()
            .antMatchers(API_URL, anySubPath(API_URL)).fullyAuthenticated()
            .antMatchers(JAVA_MELODY_URL, anySubPath(JAVA_MELODY_URL)).hasRole(Role.ADMIN.name)
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(Role.ADMIN.name)
            .anyRequest().denyAll().and()

    @Configuration
    @Order(1)
    inner class PublicURLConfigurer : WebSecurityConfigurerAdapter() {

        @Throws(Exception::class)
        override fun configure(http: HttpSecurity) {
            http.requestMatcher(
                    AntPathRequestMatcher(CSP_REPORT_URL)
            )
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable()
        }
    }
}
