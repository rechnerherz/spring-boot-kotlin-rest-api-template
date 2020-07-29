package at.rechnerherz.example.domain.account.auth

import at.rechnerherz.example.config.AUTHENTICATED_ACCOUNT_URL
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.account.AuthenticatedAccountProjection
import at.rechnerherz.example.domain.base.KProjectionFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Provides a public endpoint to access the authenticated account.
 */
@RestController
class AuthenticationController(
    private val accountRepository: AccountRepository,
    private val projectionFactory: KProjectionFactory
) {

    /**
     * Returns status 200 with the authenticated account, or 204 with an empty body when unauthenticated.
     */
    @GetMapping(path = [AUTHENTICATED_ACCOUNT_URL])
    @Transactional(readOnly = true)
    fun account(): ResponseEntity<AuthenticatedAccountProjection?> {
        val account = accountRepository.findAuthenticated()
        return if (account != null) {
            ResponseEntity(projectionFactory.createProjection(AuthenticatedAccountProjection::class, account), HttpStatus.OK)
        } else
            ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
