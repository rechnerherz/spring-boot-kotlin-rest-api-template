package at.rechnerherz.example.base

import at.rechnerherz.example.domain.account.Role.Companion.ROLE_ADMIN
import at.rechnerherz.example.init.ADMIN_PASSWORD
import at.rechnerherz.example.init.ADMIN_USERNAME
import org.springframework.security.test.context.support.WithMockUser

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@WithMockUser(username = ADMIN_USERNAME, password = ADMIN_PASSWORD, authorities = [ROLE_ADMIN])
annotation class WithMockAdmin
