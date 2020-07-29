package at.rechnerherz.example.web

import at.rechnerherz.example.config.API_URL
import at.rechnerherz.example.config.DEVELOPMENT
import at.rechnerherz.example.config.ROOT_URL
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletResponse

/**
 * Development endpoints.
 */
@Controller
@Profile(DEVELOPMENT)
class DevelopmentController {

    /**
     * Redirect root to API to access the HAL browser.
     */
    @GetMapping(path = [ROOT_URL])
    fun homeRedirect(
        response: HttpServletResponse
    ): RedirectView {
        return RedirectView(API_URL)
    }

}
