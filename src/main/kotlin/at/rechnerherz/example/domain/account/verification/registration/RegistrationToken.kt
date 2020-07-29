package at.rechnerherz.example.domain.account.verification.registration

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.account.verification.VerificationToken
import at.rechnerherz.example.domain.permission.AdminOnlyRule
import at.rechnerherz.example.domain.permission.WithPermissionRule
import javax.persistence.Entity

@Entity
@WithPermissionRule(AdminOnlyRule::class)
class RegistrationToken(
    account: Account
) : VerificationToken(null, account)
