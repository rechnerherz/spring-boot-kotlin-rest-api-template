package at.rechnerherz.example.domain.base

import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

/**
 * Custom repository base class, registered in [at.rechnerherz.example.config.JpaConfig].
 *
 * All internal methods must be called from within an existing transaction ([Propagation.MANDATORY]).
 *
 * Implements the [InternalRepository] by using the default implementation from [SimpleJpaRepository].
 */
@NoRepositoryBean
class InternalRepositoryImpl<T, ID>(
    entityInformation: JpaEntityInformation<T, *>,
    entityManager: EntityManager
) :
    SimpleJpaRepository<T, ID>(entityInformation, entityManager),
    InternalRepository<T, ID> {

    /*-------------------------------------*\
     * Read
    \*-------------------------------------*/

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    override fun internalFindAll(): MutableList<T> =
        super.findAll()

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    override fun internalFindAllById(ids: Iterable<ID>): MutableList<T> =
        super.findAllById(ids)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    override fun internalFindById(id: ID): Optional<T> =
        super.findById(id)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    override fun internalFindByIdOrNull(id: ID): T? =
        super.findById(id).orElse(null)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    override fun internalExistsById(id: ID): Boolean =
        super.existsById(id)

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    override fun internalCount(): Long =
        super.count()

    /*-------------------------------------*\
     * Save
    \*-------------------------------------*/

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    override fun <S : T> internalSave(entity: S): S =
        super.save(entity)

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    override fun <S : T> internalSaveAll(entities: Iterable<S>): MutableList<S> =
        super.saveAll(entities)

    /*-------------------------------------*\
     * Delete
    \*-------------------------------------*/

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    override fun internalDeleteById(id: ID) =
        super.deleteById(id)

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    override fun internalDelete(entity: T) =
        super.delete(entity)

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    override fun internalDeleteAll(entities: Iterable<T>) =
        super.deleteAll(entities)

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    override fun internalDeleteAll() =
        super.deleteAll()

}
