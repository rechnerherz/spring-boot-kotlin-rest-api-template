package at.rechnerherz.example.domain.account

import at.rechnerherz.example.domain.base.BaseProjection
import org.springframework.data.rest.core.config.Projection
import java.time.Instant

@Projection(name = "authenticated", types = [Account::class])
interface AuthenticatedAccountProjection : BaseProjection {

    val username: String
    val role: Role
    val registrationSubmitted: Instant?
    val registrationConfirmed: Instant?
    val secondToLastLogin: Instant?
    val lastLogin: Instant?
}

