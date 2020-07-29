package at.rechnerherz.example.domain.permission.meta

import at.rechnerherz.example.domain.account.Role.Companion.ROLE_ADMIN
import at.rechnerherz.example.domain.permission.Permission.Companion.ENTITY_TARGET
import at.rechnerherz.example.domain.permission.Permission.Companion.WRITE_PERMISSION
import org.springframework.security.access.prepost.PreAuthorize

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@PreAuthorize("hasRole('$ROLE_ADMIN') or hasPermission($ENTITY_TARGET, $WRITE_PERMISSION)")
annotation class HasRoleAdminOrHasWriteEntityPermission
