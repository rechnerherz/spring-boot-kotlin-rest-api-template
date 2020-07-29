package at.rechnerherz.example.domain.base

import at.rechnerherz.example.config.aop.NoProfiling
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.support.Repositories
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Service
import org.springframework.web.context.WebApplicationContext
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.metamodel.Attribute
import javax.persistence.metamodel.EntityType
import javax.persistence.metamodel.PluralAttribute
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberProperties

/**
 * A service that can delegate to the right repository for a given entity type.
 *
 * Also provides methods to access the entity meta-model with reflection.
 */
@Service
@NoProfiling
class GenericRepositoryService(
    webApplicationContext: WebApplicationContext,
    val entityManager: EntityManager
) {

    val repositories: Repositories = Repositories(webApplicationContext)

    /*------------------------------------*\
     * CRUD
    \*------------------------------------*/

    final inline fun <reified T : BaseEntity> save(entity: T): T =
        repositoryForEntityType(T::class).internalSave(entity)

    final inline fun <reified T : BaseEntity> saveAll(entities: Iterable<T>): Iterable<T> =
        repositoryForEntityType(T::class).internalSaveAll(entities)

    final inline fun <reified T : BaseEntity> findAll(type: KClass<T>): Iterable<T> =
        repositoryForEntityType(type).internalFindAll()

    final inline fun <reified T : BaseEntity> findById(type: KClass<T>, id: Long): T? =
        repositoryForEntityType(type).internalFindById(id).orElse(null)

    /*------------------------------------*\
     * Specifications
    \*------------------------------------*/

    final inline fun <reified T : BaseEntity> findOne(type: KClass<T>, spec: Specification<T>): Optional<T> =
        repositoryForEntityType(type).findOne(spec)

    final inline fun <reified T : BaseEntity> findAll(type: KClass<T>, spec: Specification<T>): Iterable<T> =
        repositoryForEntityType(type).findAll(spec)

    final inline fun <reified T : BaseEntity> findAll(
        type: KClass<T>,
        spec: Specification<T>,
        pageable: Pageable
    ): Page<T> =
        repositoryForEntityType(type).findAll(spec, pageable)

    final inline fun <reified T : BaseEntity> findAll(
        type: KClass<T>,
        spec: Specification<T>,
        sort: Sort
    ): Iterable<T> =
        repositoryForEntityType(type).findAll(spec, sort)

    final inline fun <reified T : BaseEntity> count(type: KClass<T>, spec: Specification<T>): Long =
        repositoryForEntityType(type).count(spec)

    /*------------------------------------*\
     * Helper Methods
    \*------------------------------------*/

    /**
     * Detach an entity and retrieve it from the database by id.
     *
     * Useful in @BeforeSave event handlers to obtain the entity state to compare to the event handler parameter.
     */
    final inline fun <reified T : SimpleBaseEntity> getPreviousEntityState(type: KClass<T>, entity: T): T {
        entityManager.detach(entity)
        return findById(type, entity.id!!)!!
    }

    /**
     * Return the repository from [Repositories] for the given entity [type].
     *
     * Throws an exception if [Repositories.getRepositoryFor] returns null or the repository is not a [BaseRepository].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseEntity> repositoryForEntityType(type: KClass<T>): BaseRepository<T> =
        repositories.getRepositoryFor(type.java).orElse(null) as? BaseRepository<T>
            ?: throw IllegalArgumentException("Could not determine repository for $type")

    /**
     * Returns a list of property accessors for all embeddable attributes of type [type] for all entities.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> allEmbeddableAttributesOfType(type: KClass<T>): List<KProperty1<BaseEntity, T?>> =
        entityManager.metamodel.entities.flatMap { entityType ->
            embeddableAttributesOfType(entityType, type).map { attribute ->
                entityType.javaType.kotlin.memberProperties.find { it.name == attribute.name } as KProperty1<BaseEntity, T?>
            }
        }

    /**
     * Returns a list of property accessors for all element collection attributes of type [type] for all entities.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> allElementCollectionAttributesOfType(type: KClass<T>): List<KProperty1<BaseEntity, Collection<T>>> =
        entityManager.metamodel.entities.flatMap { entityType ->
            elementCollectionAttributesOfType(entityType, type).map { attribute ->
                entityType.javaType.kotlin.memberProperties.find { it.name == attribute.name } as KProperty1<BaseEntity, Collection<T>>
            }
        }

    /**
     * Returns a map of all [EntityType]s to [RepositoryRestResource] annotations for all repositories.
     */
    fun getRepositoryRestResources(): Map<EntityType<*>, RepositoryRestResource?> =
        entityManager.metamodel.entities.associateWith { entityType ->
            repositories.getRepositoryFor(entityType.bindableJavaType).orElse(null)
                ?.javaClass?.interfaces?.firstOrNull()?.kotlin?.findAnnotation<RepositoryRestResource>()
        }

    private fun <T : Any> embeddableAttributesOfType(
        entityType: EntityType<T>,
        type: KClass<*>
    ): List<Attribute<in T, *>> =
        entityType.attributes.filter {
            it.persistentAttributeType == Attribute.PersistentAttributeType.EMBEDDED
                    && it.javaType == type.java
        }

    private fun <T> elementCollectionAttributesOfType(
        entityType: EntityType<T>,
        type: KClass<*>
    ): List<Attribute<in T, *>> =
        entityType.attributes.filter {
            it.persistentAttributeType == Attribute.PersistentAttributeType.ELEMENT_COLLECTION
                    && (it as? PluralAttribute<*, *, *>)?.elementType?.javaType == type.java
        }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> KProperty1<T, *>.instanceClass(): KClass<T>? =
    instanceParameter?.type?.classifier as? KClass<T>

