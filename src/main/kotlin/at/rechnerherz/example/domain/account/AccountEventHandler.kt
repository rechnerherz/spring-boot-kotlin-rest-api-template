package at.rechnerherz.example.domain.account

import at.rechnerherz.example.config.auth.isFullyAuthenticated
import at.rechnerherz.example.domain.base.GenericRepositoryService
import at.rechnerherz.example.domain.base.deletable.EntityNotDeletableException
import org.springframework.data.rest.core.annotation.HandleBeforeDelete
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@RepositoryEventHandler
@Component
class AccountEventHandler(
    private val accountRepository: AccountRepository,
    private val genericRepositoryService: GenericRepositoryService
) {

    /**
     * - Only admins are allowed to change [Account.username] directly.
     * - Prevent deactivating own account.
     */
    @HandleBeforeSave
    @Transactional(readOnly = true)
    fun beforeSave(account: Account) {
        if (!isFullyAuthenticated(Role.ADMIN)) {
            val original: Account = genericRepositoryService.getPreviousEntityState(
                Account::class, account)
            if (original.username != account.username)
                throw AccessDeniedException("Not allowed to change Account username")
        }

        if (account == accountRepository.findAuthenticated() && !account.active)
            throw EntityNotDeletableException("Cannot deactivate own account", "error.delete.body.conflict")
    }

    /**
     * Prevent deleting own account.
     */
    @HandleBeforeDelete
    @Transactional(readOnly = true)
    fun beforeDelete(account: Account) {
        if (account == accountRepository.findAuthenticated())
            throw EntityNotDeletableException("Cannot delete own account", "error.delete.body.conflict")
    }
}
