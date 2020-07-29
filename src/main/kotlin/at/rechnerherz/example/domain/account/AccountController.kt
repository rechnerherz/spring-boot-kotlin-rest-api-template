package at.rechnerherz.example.domain.account

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class AccountController(
    private val accountRepository: AccountRepository
) {

    /**
     * Check whether the account username is already in use.
     */
    @GetMapping(path = ["/public/checkDuplicateUsername"])
    @Transactional(readOnly = true)
    fun checkDuplicateUsername(
        @RequestParam username: String,
        @RequestParam(required = false) id: Long?
    ): ResponseEntity<Void> {

        // username does not exist
        val account: Account = accountRepository.findByUsername(username)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        // username exists but user is currently editing this account
        if (id != null && account.id == id)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity(HttpStatus.OK)
    }

    data class ChangePasswordBody(val passwordOld: String, val passwordNew: String)

    /**
     * Change own account password.
     */
    @PostMapping(path = ["/api/accounts/change-password"])
    @Transactional(readOnly = false)
    fun changePassword(
        @RequestBody body: ChangePasswordBody
    ): ResponseEntity<Void> {

        val (passwordOld, passwordNew) = body

        val account = accountRepository.findAuthenticated()
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)

        if (!account.passwordMatches(passwordOld))
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        account.password = passwordNew
        accountRepository.internalSave(account)

        return ResponseEntity(HttpStatus.OK)
    }

}
