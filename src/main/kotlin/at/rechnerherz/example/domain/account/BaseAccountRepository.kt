package at.rechnerherz.example.domain.account

import at.rechnerherz.example.domain.base.BaseRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional

@NoRepositoryBean
@Transactional(readOnly = true)
interface BaseAccountRepository<T : Account> : BaseRepository<T>
