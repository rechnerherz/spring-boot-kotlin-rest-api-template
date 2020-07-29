package at.rechnerherz.example.domain.permission.meta

import at.rechnerherz.example.domain.account.Role.Companion.ROLE_ADMIN
import at.rechnerherz.example.domain.permission.Permission.Companion.READ_PERMISSION
import org.springframework.security.access.prepost.PreAuthorize

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@PreAuthorize("hasRole('$ROLE_ADMIN') or hasPermission(#accountId, 'at.rechnerherz.example.domain.account.Account', $READ_PERMISSION)")
annotation class HasRoleAdminOrHasReadAccountIdPermission
