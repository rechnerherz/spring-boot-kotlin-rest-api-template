package at.rechnerherz.example.domain.permission

import at.rechnerherz.example.domain.account.Account

/**
 * Interface for entities that have a relation to an account to evaluate in permission rules.
 */
interface AccountRelation {

    val account: Account?
}
