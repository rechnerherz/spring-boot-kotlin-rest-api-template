package at.rechnerherz.example.domain.account.admin

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.account.Role
import at.rechnerherz.example.domain.account.contact.ContactData
import at.rechnerherz.example.domain.account.contact.PersonalData
import at.rechnerherz.example.domain.permission.OwnAccountRule
import at.rechnerherz.example.domain.permission.WithPermissionRule
import javax.persistence.Entity

/**
 * An admin account.
 */
@Entity
@WithPermissionRule(OwnAccountRule::class)
class Admin(
    username: String,
    password: String,
    personalData: PersonalData,
    contactData: ContactData
) : Account(
    username,
    password,
    personalData,
    contactData
) {

    override val abbreviation
        get() = "A"

    override val role: Role
        get() = Role.ADMIN

}
