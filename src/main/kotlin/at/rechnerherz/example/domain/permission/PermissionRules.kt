package at.rechnerherz.example.domain.permission

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.base.BaseEntity
import at.rechnerherz.example.domain.base.equalsById

/**
 * Interface for permission rules.
 *
 * Each rule may permit access on single items and/or collections by overriding the respective method and returning `true`.
 */
interface PermissionRule {

    fun permitItemAccess(account: Account, target: BaseEntity, permission: Permission): Boolean = false
    fun permitCollectionAccess(account: Account, permission: Permission): Boolean = false
}

/**
 * Don't allow any access except for admins.
 */
object AdminOnlyRule : PermissionRule

/**
 * Only permit read access.
 */
object ReadOnlyRule : PermissionRule {

    override fun permitItemAccess(account: Account, target: BaseEntity, permission: Permission): Boolean =
        permission == Permission.READ

    override fun permitCollectionAccess(account: Account, permission: Permission): Boolean =
        permission == Permission.READ
}

/**
 * Only permit access to the target account entity if it matches the authenticated account.
 */
object OwnAccountRule : PermissionRule {

    override fun permitItemAccess(account: Account, target: BaseEntity, permission: Permission): Boolean =
        target is Account && target equalsById account
}

/**
 * Only permit access to the target account entity if the related account matches the authenticated account.
 */
object RelatedAccountRule : PermissionRule {

    override fun permitItemAccess(account: Account, target: BaseEntity, permission: Permission): Boolean =
        target is AccountRelation && target.account equalsById account
}
