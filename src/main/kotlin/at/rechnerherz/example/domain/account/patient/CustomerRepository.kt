package at.rechnerherz.example.domain.account.patient

import at.rechnerherz.example.domain.account.BaseAccountRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.transaction.annotation.Transactional

@RepositoryRestResource(path = "customers", collectionResourceRel = "customers")
@Transactional(readOnly = true)
interface CustomerRepository : BaseAccountRepository<Customer>
