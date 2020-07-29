package at.rechnerherz.example.config.auth

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.account.AccountRepository
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

/**
 * After successful authentication with a [UsernamePasswordAuthenticationToken],
 * set [Account.secondToLastLogin] and [Account.lastLogin] and clear failed login attempts in the [LoginAttemptService].
 */
@Component
class AuthenticationSuccessListener(
    private val accountRepository: AccountRepository,
    private val loginAttemptService: LoginAttemptService
) : ApplicationListener<AuthenticationSuccessEvent> {

    @Transactional(readOnly = false)
    override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
        if (event.source is UsernamePasswordAuthenticationToken) {
            val account: Account? = accountRepository.findByAuthentication(event.authentication)

            if (account != null) {
                account.secondToLastLogin = account.lastLogin
                account.lastLogin = Instant.now()
                accountRepository.internalSave(account)
            }

            val ip: String = event.authentication.remoteAddress() ?: "[unknown]"
            val username: String = event.authentication.name
            loginAttemptService.loginSucceeded(ip, username)
        }
    }
}
