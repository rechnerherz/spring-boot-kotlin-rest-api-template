package at.rechnerherz.example.domain.base

import at.rechnerherz.example.domain.permission.meta.*
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.rest.core.annotation.RestResource
import java.util.*

/**
 * Extends [CrudRepository] for generic CRUD operations, and overrides all methods to add method security expressions.
 *
 * [Method Security Expressions](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#method-security-expressions)
 */
@NoRepositoryBean
interface SecuredCrudRepository<T, ID> : CrudRepository<T, ID> {

    /*-------------------------------------*\
     * Read
    \*-------------------------------------*/

    @RestResource
    @HasRoleAdminOrHasReadCollectionPermission
    override fun findAll(): Iterable<T>

    @RestResource
    @HasRoleAdminOrHasReadCollectionPermission
    override fun findAllById(ids: Iterable<ID>): Iterable<T>

    @RestResource
    @HasRoleAdminOrHasReadIdPermission
    override fun findById(id: ID): Optional<T>

    @RestResource
    @HasRoleAdminOrHasReadIdPermission
    override fun existsById(id: ID): Boolean

    @RestResource
    @HasRoleAdminOrHasReadCollectionPermission
    override fun count(): Long

    /*-------------------------------------*\
     * Write
    \*-------------------------------------*/

    @RestResource
    @HasRoleAdminOrHasWriteEntityPermission
    override fun <S : T> save(entity: S): S

    @RestResource
    @HasRoleAdminOrHasWriteCollectionPermission
    override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S>

    /*-------------------------------------*\
     * Delete
    \*-------------------------------------*/

    @RestResource
    @HasRoleAdminOrHasDeleteIdPermission
    override fun deleteById(id: ID)

    @RestResource
    @HasRoleAdminOrHasDeleteEntityPermission
    override fun delete(entity: T)

    @RestResource
    @HasRoleAdminOrHasDeleteCollectionPermission
    override fun deleteAll(entities: Iterable<T>)

    @RestResource
    @HasRoleAdminOrHasDeleteCollectionPermission
    override fun deleteAll()

}
