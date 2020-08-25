package at.rechnerherz.example.domain.base

import org.hibernate.Session
import javax.persistence.EntityManager
import kotlin.reflect.KClass

/**
 * Fetch an entity by its [org.hibernate.annotations.NaturalId], using [org.hibernate.Session.bySimpleNaturalId].
 *
 * [Natural Id API](http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#naturalid-api)
 */
fun <T : BaseEntity> EntityManager.findBySimpleNaturalId(entityClass: KClass<T>, simpleNaturalId: Any): T? =
    unwrap(Session::class.java)
        .bySimpleNaturalId(entityClass.java)
        .load(simpleNaturalId)

/**
 * Detach an entity, and retrieve the previous entity state from the database.
 */
fun <T : BaseEntity> EntityManager.findPreviousEntityState(entity: T): T {
    detach(entity)
    return find(entity.javaClass, entity.id)
}
