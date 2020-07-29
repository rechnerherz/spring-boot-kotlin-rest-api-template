package at.rechnerherz.example.domain.account.verification

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.base.SimpleBaseEntity
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.persistence.Entity
import javax.validation.constraints.AssertTrue

/**
 * Base entity for verification tokens.
 */
@Entity
@Immutable
abstract class VerificationToken(
    expiry: Instant?,
    account: Account
) : SimpleBaseEntity() {

    override val abbreviation
        get() = "VT"

    override val keyProperties
        get() = listOf(VerificationToken::token)

    /**
     * Universally unique token.
     */
    @NaturalId(mutable = false)
    @Column(nullable = false, unique = true, updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    val token: UUID = UUID.randomUUID()

    /**
     * The time of expiration, or null if valid forever.
     */
    @Column(updatable = false)
    val expiry: Instant? = expiry

    /**
     * Whether the token is expired (expiry time before now).
     */
    val expired: Boolean
        get() = expiry?.isBefore(Instant.now()) == true

    /*------------------------------------*\
     * Relations
    \*------------------------------------*/

    /**
     * A verification token is always issued for an account.
     *
     * When an account is deleted, all related verification tokens are deleted as well.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    val account: Account = account

    /*------------------------------------*\
     * Validations
    \*------------------------------------*/

    @AssertTrue(message = "cannot be deactivated")
    @JsonIgnore
    fun isActive() =
        active
}
