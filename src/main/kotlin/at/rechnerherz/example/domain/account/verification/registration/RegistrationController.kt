package at.rechnerherz.example.domain.account.verification.registration

import at.rechnerherz.example.config.PUBLIC_URL
import at.rechnerherz.example.domain.account.AccountRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(path = [PUBLIC_URL])
class RegistrationController(
    private val accountRepository: AccountRepository,
    private val registrationService: RegistrationService,
    private val registrationTokenRepository: RegistrationTokenRepository
) {

    data class RegistrationDTO(val username: String, val password: String)

    @PostMapping(path = ["/register"])
    @Transactional(readOnly = false)
    fun registration(
        @RequestBody body: RegistrationDTO,
        locale: Locale,
        request: HttpServletRequest
    ): ResponseEntity<Void> {

        if (registrationService.registration(locale, body.username, body.password) != null)
            return ResponseEntity(HttpStatus.OK)

        return ResponseEntity(HttpStatus.CONFLICT)
    }

    data class RegistrationConfirmationDTO(val token: UUID)

    @PostMapping(path = ["/confirm-registration"])
    @Transactional(readOnly = false)
    fun confirmRegistration(
        @RequestBody body: RegistrationConfirmationDTO
    ): ResponseEntity<Void> {

        val (token) = body

        val registrationToken = registrationTokenRepository.findByToken(token)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val account = registrationToken.account

        if (account.registrationSubmitted == null || account.registrationConfirmed != null)
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        account.registrationConfirmed = Instant.now()
        account.active = true
        accountRepository.internalSave(account)

        registrationTokenRepository.internalDelete(registrationToken)

        return ResponseEntity(HttpStatus.OK)
    }

}
