package at.rechnerherz.example.domain.base

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

/**
 * Base class for all repositories.
 *
 * - Extends [InternalRepository] for unsecured internal CRUD operations.
 * - Extends [SecuredCrudRepository] for secured exported CRUD operations.
 * - Extends [JpaSpecificationExecutor] to execute [org.springframework.data.jpa.domain.Specification] queries (unsecured internal).
 */
@NoRepositoryBean
interface BaseRepository<T : BaseEntity> :
    InternalRepository<T, Long>,
    SecuredCrudRepository<T, Long>,
    JpaSpecificationExecutor<T>
