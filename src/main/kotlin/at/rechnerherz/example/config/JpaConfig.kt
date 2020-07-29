package at.rechnerherz.example.config

import at.rechnerherz.example.Application
import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.base.InternalRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*

const val TRANSACTION_ADVICE_ORDER = 0

/**
 * Spring Data JPA configuration.
 *
 * [EnableJpaRepositories.repositoryBaseClass] sets the customized base repository implementation to [InternalRepositoryImpl].
 *
 * [EnableJpaAuditing] enables JPA auditing via annotations [CreatedDate], [CreatedBy], [LastModifiedDate], and [LastModifiedBy].
 * Entities must be annotated with an [AuditingEntityListener] to be audited.
 *
 * Important notes regarding Spring annotation-based transactions, when using the default proxy-mode:
 *
 *  - @Transactional only works on public methods.
 *  - Only external method calls coming in through the proxy are intercepted.
 *    Self-invocation of a @Transactional method will not start a transaction.
 *  - The proxy must be fully initialized, don't rely on this feature in your initialization code, i.e. @PostConstruct.
 *  - It's not recommended to use @Transactional on interfaces (repositories are an exception).
 *
 * [Customize the Base Repository](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.customize-base-repository),
 * [AuditorAware](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#auditing.auditor-aware),
 * [Using @Transactional](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#transaction-declarative-annotations),
 * [Spring Data JPA Transactionality](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#transactions)
 */
@Configuration
@EnableJpaRepositories(
    repositoryBaseClass = InternalRepositoryImpl::class,
    basePackageClasses = [Application::class]
)
@EnableJpaAuditing
@EnableTransactionManagement(order = TRANSACTION_ADVICE_ORDER)
class JpaConfig(
    private val accountRepository: AccountRepository
) {

    /**
     * Provides the authenticated account used for [CreatedBy] and [LastModifiedBy].
     */
    @Bean
    fun accountAuditor(): AuditorAware<Account> =
        AuditorAware<Account> { Optional.ofNullable(accountRepository.findAuthenticated()) }

}
