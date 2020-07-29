package at.rechnerherz.example.domain.account

import at.rechnerherz.example.domain.account.Role.Companion.ROLE_PREFIX
import org.springframework.security.core.GrantedAuthority

/**
 * An implementation of [GrantedAuthority] for role based permissions.
 *
 * The string representation of this authority is [ROLE_PREFIX] ( `"ROLE_"` ) followed by the enum constant name, e.g. `"ROLE_ADMIN"`.
 *
 * The `"ROLE_"` prefix is used by the Spring Security expressions like `hasRole` or `hasAnyRole`, as defined in [org.springframework.security.access.expression.SecurityExpressionRoot].
 *
 * [What does “ROLE_” mean and why do I need it on my role names?](https://docs.spring.io/spring-security/site/faq/faq.html#faq-role-prefix),
 * [GrantedAuthority](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#tech-granted-authority),
 * [Common Built-In Expressions](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#el-common-built-in)
 */
enum class Role : GrantedAuthority {
    ADMIN,
    CUSTOMER;

    override fun getAuthority(): String =
        ROLE_PREFIX + name

    companion object {
        const val ROLE_PREFIX = "ROLE_"
        const val ROLE_ADMIN = ROLE_PREFIX + "ADMIN"
    }
}
