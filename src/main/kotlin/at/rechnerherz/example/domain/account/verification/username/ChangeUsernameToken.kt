package at.rechnerherz.example.domain.account.verification.username

import at.rechnerherz.example.config.validation.MandatoryEmail
import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.account.verification.VerificationToken
import at.rechnerherz.example.domain.permission.AdminOnlyRule
import at.rechnerherz.example.domain.permission.WithPermissionRule
import java.time.Duration
import java.time.Instant
import javax.persistence.Entity

@Entity
@WithPermissionRule(AdminOnlyRule::class)
class ChangeUsernameToken(
    account: Account,
    username: String
) : VerificationToken(Instant.now().plus(Duration.ofDays(1)), account) {

    @MandatoryEmail
    val username: String = username
}
