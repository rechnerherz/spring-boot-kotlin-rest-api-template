package at.rechnerherz.example.util

import kotlin.random.Random

/**
 * Set a collection to the given values, by clearing the collection and adding all values.
 *
 * This can be used in a setter to keep a collection object and to avoid the error
 * "A collection with cascade=”all-delete-orphan” was no longer referenced by the owning entity instance".
 */
fun <T> MutableCollection<T>.clearAndAddAll(values: Collection<T>) {
    clear()
    addAll(values)
}

/**
 * Find the first element that is an instance of the given class, or return null.
 */
inline fun <reified T> Collection<*>.findFirstInstance(): T? =
    asSequence().filterIsInstance<T>().firstOrNull()

/**
 * Return a view of the list without the first element, or an empty list if the list contains only one or zero elements.
 */
inline fun <reified T> List<T>.skipFirst(): List<T> =
    if (size > 1) subList(1, size) else emptyList()

/**
 * Return up to [max] distinct random elements from the collection.
 */
fun <T> Collection<T>.random(max: Int, random: Random = Random): Set<T> =
    (0 until max).mapTo(mutableSetOf()) { random(random) }
