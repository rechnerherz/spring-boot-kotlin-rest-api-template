package at.rechnerherz.example.domain.account.verification.registration

import at.rechnerherz.example.domain.account.verification.VerificationTokenRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface RegistrationTokenRepository : VerificationTokenRepository<RegistrationToken>
