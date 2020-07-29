package at.rechnerherz.example.domain.base.deletable

import at.rechnerherz.example.domain.base.BaseEntity
import kotlin.reflect.KClass

/**
 * Annotation to specify all entity references that prevent deletion of an entity.
 *
 * Used by [EntityNotDeletableEventHandler] to check whether it is save to delete an entity.
 */
@Target(AnnotationTarget.CLASS)
@Repeatable
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ReferencedBy(

    /**
     * Referencing entity classes.
     */
    val entity: KClass<out BaseEntity>,

    /**
     * Property name of the referenced entity.
     */
    val property: String,

    /**
     * An optional entity mapping a many-to-many join table.
     */
    val joinEntity: KClass<out Any> = Any::class,

    /**
     * Property name of the referenced entity in the join entity.
     */
    val joinProperty: String = ""
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class References(
    val value: Array<ReferencedBy>
)
