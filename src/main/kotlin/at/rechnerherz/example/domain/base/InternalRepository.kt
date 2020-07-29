package at.rechnerherz.example.domain.base

import java.util.*

/**
 * Interface for unsecured internal CRUD queries. Implemented by [InternalRepositoryImpl].
 */
interface InternalRepository<T, ID> {

    /*-------------------------------------*\
     * Read
    \*-------------------------------------*/

    fun internalFindAll(): MutableList<T>

    fun internalFindAllById(ids: Iterable<ID>): MutableList<T>

    fun internalFindById(id: ID): Optional<T>

    fun internalFindByIdOrNull(id: ID): T?

    fun internalExistsById(id: ID): Boolean

    fun internalCount(): Long

    /*-------------------------------------*\
     * Save
    \*-------------------------------------*/

    fun <S : T> internalSave(entity: S): S

    fun <S : T> internalSaveAll(entities: Iterable<S>): MutableList<S>

    /*-------------------------------------*\
     * Delete
    \*-------------------------------------*/

    fun internalDeleteById(id: ID)

    fun internalDelete(entity: T)

    fun internalDeleteAll(entities: Iterable<T>)

    fun internalDeleteAll()

    /*-------------------------------------*\
     * Other
    \*-------------------------------------*/

    fun flush()

}
