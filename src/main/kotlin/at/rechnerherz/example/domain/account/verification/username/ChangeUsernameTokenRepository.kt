package at.rechnerherz.example.domain.account.verification.username

import at.rechnerherz.example.domain.account.verification.VerificationTokenRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface ChangeUsernameTokenRepository : VerificationTokenRepository<ChangeUsernameToken>
