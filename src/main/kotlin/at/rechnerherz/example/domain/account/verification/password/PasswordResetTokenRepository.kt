package at.rechnerherz.example.domain.account.verification.password

import at.rechnerherz.example.domain.account.verification.VerificationTokenRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface PasswordResetTokenRepository : VerificationTokenRepository<PasswordResetToken>
