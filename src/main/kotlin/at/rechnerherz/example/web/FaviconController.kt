package at.rechnerherz.example.web

import at.rechnerherz.example.config.FAVICON
import at.rechnerherz.aoprofiling.NoProfiling
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class FaviconController {

    /**
     * Return 200 without a body for favicon.ico requests.
     */
    @GetMapping(FAVICON)
    @ResponseBody
    @NoProfiling
    fun noFavicon() {}
}
