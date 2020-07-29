package at.rechnerherz.example.config.error

import at.rechnerherz.example.config.CSP_REPORT_URL
import at.rechnerherz.example.config.aop.NoProfiling
import at.rechnerherz.example.util.prettyPrintJsonString
import at.rechnerherz.example.util.readToString
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpServletRequest

@Controller
class CSPReportController {

    private val log = KotlinLogging.logger {}

    @PostMapping(path = [CSP_REPORT_URL])
    @NoProfiling
    fun reportFrontendError(
        request: HttpServletRequest
    ): ResponseEntity<Void> {
        val body = prettyPrintJsonString(request.inputStream.readToString())
        val subject = "Content-Security-Policy Violation"
        log.error("$subject\n\n$body")
        return ResponseEntity(HttpStatus.OK)
    }

}
