package at.rechnerherz.example.web

import at.rechnerherz.example.config.DEVELOPMENT
import at.rechnerherz.example.config.PUBLIC_URL
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.RequestContext
import java.util.*
import javax.money.Monetary
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Endpoints to access and change the user locale and time zone (for testing/debugging).
 *
 * [Method Arguments](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-arguments)
 */
@RestController
@RequestMapping(path = [PUBLIC_URL])
@Profile(DEVELOPMENT)
class LocaleController {

    data class GetLocaleDTO(val languageTag: String, val timeZoneID: String, val currency: String)

    @GetMapping(path = ["/locale"])
    fun getLocale(
        locale: Locale?,
        timeZone: TimeZone?
    ): GetLocaleDTO {
        val currencyUnit = locale?.let { Monetary.getCurrency(locale) }
        return GetLocaleDTO(
            locale?.toLanguageTag() ?: "",
            timeZone?.id ?: "",
            currencyUnit?.currencyCode ?: ""
        )
    }

    data class SetLocaleDTO(val languageTag: String, val timeZoneID: String)

    @PostMapping(path = ["/locale"])
    fun setLocale(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestBody body: SetLocaleDTO
    ): GetLocaleDTO {
        val requestContext = RequestContext(request, response)
        requestContext.changeLocale(Locale.forLanguageTag(body.languageTag), TimeZone.getTimeZone(body.timeZoneID))
        return getLocale(requestContext.locale, requestContext.timeZone)
    }
}
