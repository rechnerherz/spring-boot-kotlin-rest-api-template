package at.rechnerherz.example.domain.account.verification

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.base.BaseRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional
import java.util.*

@NoRepositoryBean
@Transactional(readOnly = true)
interface VerificationTokenRepository<T : VerificationToken> : BaseRepository<T> {

    fun findByToken(token: UUID): T?

    fun findByAccount(account: Account): List<T>
}
