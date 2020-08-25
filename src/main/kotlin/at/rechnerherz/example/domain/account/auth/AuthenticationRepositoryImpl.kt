package at.rechnerherz.example.domain.account.auth

import at.rechnerherz.example.config.auth.authentication
import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.base.findBySimpleNaturalId
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * Implementation of [AuthenticationRepository].
 */
@Component
@Transactional(readOnly = true)
class AuthenticationRepositoryImpl(
    private val entityManager: EntityManager
) : AuthenticationRepository {

    override fun findAuthenticated(): Account? =
        findByAuthentication(authentication())

    override fun findByAuthentication(authentication: Authentication?): Account? =
        authentication?.let { findByUsername(it.name) }

    override fun findByUsername(username: String): Account? =
        entityManager.findBySimpleNaturalId(Account::class, username)

}
