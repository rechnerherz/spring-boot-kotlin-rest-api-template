package at.rechnerherz.example.domain.permission.meta

import at.rechnerherz.example.domain.account.Role.Companion.ROLE_ADMIN
import at.rechnerherz.example.domain.permission.Permission.Companion.DELETE_PERMISSION
import at.rechnerherz.example.domain.permission.Permission.Companion.ID_TARGET
import org.springframework.security.access.prepost.PreAuthorize

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@PreAuthorize("hasRole('$ROLE_ADMIN') or hasPermission($ID_TARGET, $DELETE_PERMISSION)")
annotation class HasRoleAdminOrHasDeleteIdPermission
