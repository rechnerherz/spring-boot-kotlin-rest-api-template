package at.rechnerherz.example.domain.base.deletable

import at.rechnerherz.example.domain.base.BaseEntity
import at.rechnerherz.example.domain.base.GenericRepositoryService
import at.rechnerherz.example.util.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.rest.core.annotation.HandleBeforeDelete
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.lang.reflect.Modifier
import javax.persistence.EntityManager
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

/**
 * Before deleting an entity, throw an [EntityNotDeletableException] if it is referenced by any other entity specified in a [ReferencedBy] annotation.
 */
@RepositoryEventHandler
@Component
@Suppress("UNCHECKED_CAST")
class EntityNotDeletableEventHandler(
    private val entityManager: EntityManager,
    private val genericRepositoryService: GenericRepositoryService
) {

    @HandleBeforeDelete
    @Transactional(readOnly = true, noRollbackFor = [EntityNotDeletableException::class])
    fun beforeDelete(entity: BaseEntity) {
        // Check annotations on the entity
        checkAnnotations(entity)

        // Check annotations on all supertypes of the entity
        entity::class.supertypes.forEach {
            checkAnnotations(entity, it.classifier as KClass<out BaseEntity>)
        }
    }

    private fun checkAnnotations(entity: BaseEntity, supertype: KClass<out BaseEntity>? = null) {
        val type = supertype ?: entity::class

        val references = type.findAnnotation<References>()
        references?.value?.forEach { checkReferencedBy(entity, it) }

        val referencedBy = type.findAnnotation<ReferencedBy>()
        referencedBy?.let { checkReferencedBy(entity, referencedBy) }
    }

    private fun checkReferencedBy(entity: BaseEntity, referencedBy: ReferencedBy) {
        val type = referencedBy.entity as KClass<BaseEntity>
        val joinType = if (referencedBy.joinEntity === Any::class) null else referencedBy.joinEntity as KClass<Any>
        checkReferencedByType(entity, referencedBy.property, type, joinType, referencedBy.joinProperty)
    }

    private fun checkReferencedByType(
        entity: BaseEntity,
        propertyName: String,
        type: KClass<BaseEntity>,
        joinType: KClass<Any>? = null,
        joinPropertyName: String? = null
    ) {

        // If type is BaseEntity, check recursively for all BaseEntities
        if (type == BaseEntity::class) {
            entityManager.metamodel.entities.filter { !Modifier.isAbstract(it.bindableJavaType.modifiers) }.forEach {
                if (it.bindableJavaType.kotlin.isSubclassOf(BaseEntity::class))
                    checkReferencedByType(entity, propertyName, it.bindableJavaType.kotlin as KClass<BaseEntity>)
            }
        } else {
            val property = type.memberProperties.find { it.name == propertyName }
                ?: throw RuntimeException("Property '$propertyName' not found don type $type.")

            // Build the specification
            val spec: Specification<BaseEntity> =

                if (joinType != null) {
                    val joinProperty = joinType.memberProperties.find { it.name == joinPropertyName }
                        ?: throw RuntimeException("Property '$propertyName' not found don type $type.")

                    // Join the property and get the check where join property is equal to the entity
                    if ((property.returnType.classifier as KClass<*>).isSubclassOf(Collection::class))
                        where { equal(it.join((property as KProperty1<BaseEntity, Collection<*>>)).get(joinProperty), entity) }
                    else
                        throw NotImplementedError()

                } else {

                    // For a collection property check if the entity is a member of the collection
                    if ((property.returnType.classifier as KClass<*>).isSubclassOf(Collection::class))
                        (property as KProperty1<BaseEntity, Collection<*>>).isMember(entity)
                    // Otherwise check for equality
                    else
                        property.equal(entity)

                }

            // Execute a count query with the specification
            val count = genericRepositoryService.count(type, spec)
            if (count > 0)
                throw EntityNotDeletableException(
                    "$entity cannot be deleted, it is referenced by $count ${type.simpleName}(s)",
                    "error.delete.body.conflict",
                    mapOf(
                        "entity" to entity.className.decapitalize(),
                        "identifier" to entity.identifier,
                        "referenced_by_count" to count.toString(),
                        "referenced_by_type" to type.simpleName.toString().decapitalize()
                    )
                )
        }
    }
}
