package at.rechnerherz.example.domain.account.admin

import at.rechnerherz.example.domain.account.BaseAccountRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.transaction.annotation.Transactional

@RepositoryRestResource(path = "admins", collectionResourceRel = "admins")
@Transactional(readOnly = true)
interface AdminRepository : BaseAccountRepository<Admin>
