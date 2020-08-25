package at.rechnerherz.example.config.auth

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.account.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Provides the [loadUserByUsername] method to receive [UserDetails] for an [Account] from the database.
 *
 * It does not authenticate the user, which is done be the [org.springframework.security.authentication.AuthenticationManager],
 * which in turn uses one or more [org.springframework.security.authentication.AuthenticationProvider].
 *
 * Provides a [User] object as implementation for the [UserDetails],
 * where the [Account.role] is the only authority granted to the [User],
 * and [User.enabled] is set to [Account.allowedToAuthenticate].
 *
 * Setting any of the booleans ([User.enabled], [User.accountNonExpired], [User.credentialsNonExpired], [User.accountNonLocked])
 * to `false` will throw an [org.springframework.security.authentication.AccountStatusException]
 * that can be caught in [org.springframework.security.web.authentication.AuthenticationFailureHandler.onAuthenticationFailure].
 *
 * [The UserDetailsService](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#tech-userdetailsservice)
 */
class AccountUserDetailsService(
    private val accountRepository: AccountRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val account: Account = accountRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        return User(
            account.username,
            account.password,
            account.allowedToAuthenticate,
            true,
            true,
            true,
            setOf(account.role)
        )
    }

}
