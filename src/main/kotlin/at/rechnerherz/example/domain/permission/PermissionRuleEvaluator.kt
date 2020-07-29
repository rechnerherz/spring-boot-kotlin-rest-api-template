package at.rechnerherz.example.domain.permission

import at.rechnerherz.example.config.auth.username
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.base.BaseEntity
import at.rechnerherz.example.domain.base.getImplementationClass
import mu.KotlinLogging
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import javax.persistence.EntityManager
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Component
class PermissionRuleEvaluator(
    private val entityManager: EntityManager,
    private val accountRepository: AccountRepository
) : PermissionEvaluator {

    private val log = KotlinLogging.logger {}

    /**
     * Helper method to dynamically get the canonical name of the domain type from a repository object.
     *
     * Used to get the targetType for `hasPermission` expressions in
     * - [at.rechnerherz.example.domain.permission.Permission.ID_TARGET] and
     * - [at.rechnerherz.example.domain.permission.Permission.COLLECTION_TARGET].
     */
    fun resolve(repository: Any): String? =
        if (repository is Repository<*, *>)
            repository.javaClass.interfaces.firstOrNull()?.let {
                DefaultRepositoryMetadata(it).domainType.canonicalName
            }
        else throw IllegalArgumentException("$repository is not a repository")

    /**
     * Evaluation for security expressions of the form `hasPermission(id, targetType, permission)`.
     *
     * - The [authentication] object is injected.
     * - The [permission] is expected to be an enum value of [Permission].
     * - The [targetType] is expected to be an entity class.
     * - If the [targetId] is null, the collection permission rule for the [targetType] is checked.
     * - Otherwise, the entity with the given [targetId] and [targetType] is fetched and passed to the
     *   `hasPermission` method that accepts a `targetDomainObject`.
     */
    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String,
        permission: Any
    ): Boolean {

        val targetClass = classForName(targetType)
            ?: return denied("class not found", authentication, targetId, targetType, permission)

        if (permission !is Permission)
            return denied("unexpected permission", authentication, targetId, targetType, permission)

        val account = accountRepository.findByAuthentication(authentication)
            ?: return denied("unauthorized", authentication, targetId, targetType, permission)

        // collection resource
        if (targetId == null) {
            val rule = permissionRuleForTargetClass(targetClass.kotlin)
                ?: return denied("no rule defined", authentication, targetId, targetType, permission)

            if (!rule.permitCollectionAccess(account, permission))
                return denied(
                    rule::class.simpleName
                        ?: "unknown rule", authentication, targetId, targetType, permission
                )

            return granted(authentication, targetId, targetType, permission)
        }

        // single item resource
        else {
            val targetDomainObject = entityManager.find(targetClass, targetId)
                ?: return granted(authentication, targetId, targetType, permission)

            return hasPermission(authentication, targetDomainObject, permission)
        }
    }

    /**
     * Evaluation for security expressions of the form `hasPermission(targetDomainObject, permission)`.
     *
     * - The [authentication] object is injected.
     * - The [permission] is expected to be an enum value of [Permission].
     * - Then the item permission rule for the [targetDomainObject] is checked.
     */
    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any): Boolean {

        if (permission !is Permission)
            return denied("unexpected permission", authentication, targetDomainObject, permission)

        if (targetDomainObject == null)
            return granted(authentication, targetDomainObject, permission)

        var target: Any = targetDomainObject

        if (targetDomainObject is Optional<*>) {
            if (targetDomainObject.isPresent)
                target = targetDomainObject.get()
            else
                return granted(authentication, targetDomainObject, permission)
        }

        val account = accountRepository.findByAuthentication(authentication)
            ?: return denied("unauthorized", authentication, target, permission)

        if (target !is BaseEntity)
            return denied("not a base entity", authentication, target, permission)

        val rule = permissionRuleForTarget(target)
            ?: return denied("no rule defined", authentication, target, permission)

        if (!rule.permitItemAccess(account, target, permission))
            return denied(rule::class.simpleName ?: "unknown rule", authentication, target, permission)

        return granted(authentication, target, permission)
    }

    private fun permissionRuleForTargetClass(target: KClass<*>): PermissionRule? =
        target.findAnnotation<WithPermissionRule>()?.value?.objectInstance

    private inline fun <reified T : BaseEntity> permissionRuleForTarget(target: T): PermissionRule? =
        permissionRuleForTargetClass(getImplementationClass(target))

    private fun granted(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        log.debug("$permission permission granted for user ${authentication.username()} on $targetDomainObject")
        return true
    }

    private fun granted(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        log.debug("$permission permission granted for user ${authentication.username()} on $targetType with id=$targetId")
        return true
    }

    private fun denied(
        reason: String,
        authentication: Authentication?,
        targetDomainObject: Any?,
        permission: Any?
    ): Boolean {
        log.warn("$permission permission denied for reason: '$reason' for user ${authentication.username()} on $targetDomainObject")
        return false
    }

    private fun denied(
        reason: String,
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        log.warn("$permission permission denied for reason: '$reason' for user ${authentication.username()} on $targetType with id=$targetId")
        return false
    }

    private fun classForName(name: String): Class<*>? =
        try {
            Class.forName(name)
        } catch (e: ClassNotFoundException) {
            null
        }
}
