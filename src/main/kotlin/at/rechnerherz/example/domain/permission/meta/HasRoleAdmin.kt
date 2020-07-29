package at.rechnerherz.example.domain.permission.meta

import at.rechnerherz.example.domain.account.Role.Companion.ROLE_ADMIN
import org.springframework.security.access.prepost.PreAuthorize

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@PreAuthorize("hasRole('$ROLE_ADMIN')")
annotation class HasRoleAdmin
