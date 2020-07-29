package at.rechnerherz.example.domain.account

import at.rechnerherz.example.domain.account.auth.AuthenticationRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.transaction.annotation.Transactional

@RepositoryRestResource(path = "accounts", collectionResourceRel = "accounts")
@Transactional(readOnly = true)
interface AccountRepository : BaseAccountRepository<Account>, AuthenticationRepository
