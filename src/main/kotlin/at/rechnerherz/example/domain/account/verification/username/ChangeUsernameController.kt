package at.rechnerherz.example.domain.account.verification.username

import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.account.admin.Admin
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(path = ["/api/accounts"])
class ChangeUsernameController(
    private val accountRepository: AccountRepository,
    private val changeUsernameService: ChangeUsernameService,
    private val changeUsernameTokenRepository: ChangeUsernameTokenRepository
) {

    data class RequestChangeUsernameDTO(val username: String)

    @PostMapping(path = ["/request-change-username"])
    @Transactional(readOnly = false)
    fun requestChangeUsername(
        @RequestBody body: RequestChangeUsernameDTO,
        locale: Locale,
        request: HttpServletRequest
    ): ResponseEntity<Void> {

        val (username) = body
        val account = accountRepository.findAuthenticated()
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        if (account is Admin || !account.active)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        changeUsernameService.sendChangeUsernameMail(account, username, locale)
        return ResponseEntity(HttpStatus.OK)
    }

    data class ConfirmChangeUsernameDTO(val token: UUID)

    @PostMapping(path = ["/confirm-change-username"])
    @Transactional(readOnly = false)
    fun confirmChangeUsername(
        @RequestBody body: ConfirmChangeUsernameDTO,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<Void> {

        val (token) = body

        val verificationToken = changeUsernameTokenRepository.findByToken(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        if (verificationToken.expired)
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        val account = verificationToken.account
        account.username = verificationToken.username
        accountRepository.internalSave(account)
        changeUsernameTokenRepository.internalDelete(verificationToken)

        //TODO: authenticate with new username

        return ResponseEntity(HttpStatus.OK)
    }

}
