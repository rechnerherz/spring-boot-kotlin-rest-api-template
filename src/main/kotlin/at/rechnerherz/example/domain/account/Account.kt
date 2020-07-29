package at.rechnerherz.example.domain.account

import at.rechnerherz.example.config.validation.MandatoryEmail
import at.rechnerherz.example.domain.account.contact.ContactData
import at.rechnerherz.example.domain.account.contact.PersonalData
import at.rechnerherz.example.domain.base.BaseEntity
import at.rechnerherz.example.domain.base.SimpleBaseEntity
import at.rechnerherz.example.domain.base.deletable.ReferencedBy
import at.rechnerherz.example.domain.base.deletable.References
import at.rechnerherz.example.domain.permission.AdminOnlyRule
import at.rechnerherz.example.domain.permission.WithPermissionRule
import at.rechnerherz.example.mail.MailAddress
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.NaturalId
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.Valid
import javax.validation.constraints.NotBlank


/**
 * Base entity for user accounts.
 *
 * Uses a [org.springframework.security.crypto.password.DelegatingPasswordEncoder] to encode passwords.
 *
 * Also referenced by [at.rechnerherz.example.domain.account.verification.VerificationToken] with on-delete cascade.
 */
@Entity
@References(
    [
        ReferencedBy(entity = BaseEntity::class, property = "createdBy"),
        ReferencedBy(entity = BaseEntity::class, property = "modifiedBy")
    ]
)
@WithPermissionRule(AdminOnlyRule::class)
abstract class Account(
    username: String,
    password: String,
    personalData: PersonalData,
    contactData: ContactData
) : SimpleBaseEntity() {

    companion object {
        private val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    override val keyProperties
        get() = listOf(Account::username)

    /**
     * The account [Role]. The role is determined by the subclass and is not persisted.
     */
    abstract val role: Role

    /**
     * Returns true if the account has any of the given roles.
     *
     * Use this method instead of `instanceof`/`is` checks to avoid issues with Hibernate proxies.
     */
    fun hasAnyRole(vararg roles: Role): Boolean =
        roles.contains(role)

    /*------------------------------------*\
     * Attributes
    \*------------------------------------*/

    /**
     * The unique username, must be a valid email.
     */
    @NaturalId(mutable = true)
    @Column(nullable = false, unique = true)
    @MandatoryEmail
    var username: String = username

    /**
     * The user password, always encoded with [passwordEncoder].
     */
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    var password: String = passwordEncoder.encode(password)
        set(value) {
            field = passwordEncoder.encode(value)
        }

    /**
     * Check if the provided password matches the user password.
     */
    fun passwordMatches(password: String): Boolean =
        passwordEncoder.matches(password, this.password)

    /**
     * The time the user submitted their registration, or null if never submitted.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var registrationSubmitted: Instant? = null

    /**
     * The time the user confirmed their registration, or null if never confirmed.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var registrationConfirmed: Instant? = null

    /**
     * The second to last time the user logged in, or null if never logged in for a second time.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var secondToLastLogin: Instant? = null

    /**
     * The last time the user logged in, or null if never logged in.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var lastLogin: Instant? = null

    /**
     * The personal data of the account holder.
     */
    @Valid
    var personalData: PersonalData = personalData

    /**
     * The contact data of the account holder.
     */
    @Valid
    var contactData: ContactData = contactData

    /*------------------------------------*\
     * Logic
    \*------------------------------------*/

    /**
     * Whether the account is allowed to log in.
     */
    val allowedToAuthenticate: Boolean
        @JsonIgnore
        get() = active

    /**
     * The mail address (username) with full name as peronsal identifier.
     */
    val mailAddress: MailAddress
        get() = MailAddress(username, personalData.fullName)

}
