package at.rechnerherz.example.domain.account.verification.password

import at.rechnerherz.example.config.PUBLIC_URL
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.account.Role
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(path = [PUBLIC_URL])
class PasswordResetController(
    private val accountRepository: AccountRepository,
    private val passwordResetService: PasswordResetService,
    private val passwordResetTokenRepository: PasswordResetTokenRepository
) {

    data class RequestPasswordResetDTO(val username: String)

    @PostMapping(path = ["/request-password-reset"])
    @Transactional(readOnly = false)
    fun requestPasswordReset(
        @RequestBody body: RequestPasswordResetDTO,
        locale: Locale,
        request: HttpServletRequest
    ): ResponseEntity<Void> {

        val (username) = body
        val account = accountRepository.findByUsername(username)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        if (account.role == Role.ADMIN || !account.active)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        passwordResetService.sendPasswordResetMail(account, locale)
        return ResponseEntity(HttpStatus.OK)
    }

    data class ResetPasswordDTO(val token: UUID, val password: String)

    @PostMapping(path = ["/reset-password"])
    @Transactional(readOnly = false)
    fun resetPassword(
        @RequestBody body: ResetPasswordDTO
    ): ResponseEntity<Void> {

        val (token, password) = body

        val verificationToken = passwordResetTokenRepository.findByToken(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        if (verificationToken.expired)
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        val account = verificationToken.account
        account.password = password
        accountRepository.internalSave(account)
        passwordResetTokenRepository.internalDelete(verificationToken)

        return ResponseEntity(HttpStatus.OK)
    }

}
