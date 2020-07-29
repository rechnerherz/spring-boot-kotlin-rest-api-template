package at.rechnerherz.example.domain.base

import com.google.common.base.MoreObjects
import java.io.Serializable
import kotlin.reflect.KProperty1

/**
 * An abstract base class for entities and embeddables to implement `toString` based on the [toStringProperties].
 */
abstract class ToString : Serializable {

    protected open val toStringProperties: List<KProperty1<out Any, Any?>>
        get() = emptyList()

    override fun toString(): String =
        if (toStringProperties.isEmpty())
            super.toString()
        else
            propertiesToString(this, toStringProperties)
}

internal inline fun <reified T : Any> propertiesToString(
    obj: T,
    properties: List<KProperty1<out T, Any?>>
): String {

    val toStringHelper = MoreObjects.toStringHelper(obj::class.java.simpleName)
    propertiesCast(properties).forEach { toStringHelper.add(it.name, it.get(obj)) }
    return toStringHelper.toString()
}

internal inline fun <reified T> propertiesCast(
    properties: List<KProperty1<out T, Any?>>
): List<KProperty1<T, Any?>> =
    properties.map {
        @Suppress("UNCHECKED_CAST")
        it as KProperty1<T, Any?>
    }
