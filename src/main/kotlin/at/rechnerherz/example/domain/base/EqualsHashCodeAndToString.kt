package at.rechnerherz.example.domain.base

import kotlin.reflect.KProperty1

/**
 * An abstract base class for entities and embeddables to implement `hashCode`, `equals`, and `toString`.
 *
 * - [hashCode] and [equals] are generated from the [keyProperties].
 * - [toString] is generated from the [toStringProperties] and [keyProperties].
 *
 * [Implementing equals() and hashCode()](http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#mapping-model-pojo-equalshashcode)
 */
abstract class EqualsHashCodeAndToString : ToString() {

    protected abstract val keyProperties: List<KProperty1<out Any, Any?>>

    override fun hashCode(): Int =
        if (keyProperties.isEmpty())
            super.hashCode()
        else
            propertiesHashCode(this, keyProperties)

    override fun equals(other: Any?): Boolean =
        if (keyProperties.isEmpty())
            super.equals(other)
        else
            propertiesEquals(this, other, keyProperties)

    override fun toString(): String =
        if (toStringProperties.isEmpty() && keyProperties.isEmpty())
            super.toString()
        else
            propertiesToString(this, toStringProperties.plus(keyProperties))
}

internal inline fun <reified T : Any> propertiesHashCode(
    obj: T,
    properties: List<KProperty1<out T, Any?>>
): Int =
    propertiesCast(properties).map { it.get(obj) }.hashCode()

internal inline fun <reified T : Any> propertiesEquals(
    first: T,
    second: Any?,
    properties: List<KProperty1<out T, Any?>>
): Boolean {

    if (first === second) return true
    if (second === null || second !is T) return false
    return propertiesCast(properties).all { it.get(first) == it.get(second) }
}
