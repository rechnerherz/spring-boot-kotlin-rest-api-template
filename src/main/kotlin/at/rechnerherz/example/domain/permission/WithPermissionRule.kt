package at.rechnerherz.example.domain.permission

import kotlin.reflect.KClass

/**
 * Annotation to specify a permission rule for an entity class.
 *
 * - Each entity class must have a rule defined to be accessible in the REST API.
 * - The rules are not inherited by subclasses.
 * - The rules don't apply to admins (admins are allowed to access everything).
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class WithPermissionRule(
    val value: KClass<out PermissionRule>
)
