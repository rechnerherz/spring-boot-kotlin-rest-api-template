package at.rechnerherz.example.domain.base

import org.springframework.data.domain.Page
import org.springframework.data.projection.ProjectionInformation
import org.springframework.data.projection.SpelAwareProxyProjectionFactory
import kotlin.reflect.KClass

/**
 * Extension for the [SpelAwareProxyProjectionFactory] that accepts Kotlin class instances.
 *
 * Also provides methods to create projections of iterables and pages.
 */
open class KProjectionFactory : SpelAwareProxyProjectionFactory() {

    fun <T : Any> createProjection(projectionType: KClass<T>, source: Any): T =
        super.createProjection(projectionType.java, source)

    fun <T : Any> createProjection(projectionType: KClass<T>): T =
        super.createProjection(projectionType.java)

    fun <T : Any> createNullableProjection(projectionType: KClass<T>, source: Any?): T? =
        super.createNullableProjection(projectionType.java, source)

    fun createProjectionInformation(projectionType: KClass<*>): ProjectionInformation =
        super.createProjectionInformation(projectionType.java)

    fun <T : Any> createProjectionList(projectionType: KClass<T>, sourceList: Iterable<*>): List<T> =
        sourceList.map { source -> super.createProjection(projectionType.java, source!!) }

    fun <T : Any> createProjectionListList(
        projectionType: KClass<T>,
        sourceList: Iterable<Iterable<*>>
    ): List<List<T>> =
        sourceList.map { source -> createProjectionList(projectionType, source) }

    fun <T : Any> createNullableProjectionList(projectionType: KClass<T>, sourceList: Iterable<*>): List<T?> =
        sourceList.map { source -> super.createNullableProjection(projectionType.java, source) }

    fun <T : Any> createProjectionPage(projectionType: KClass<T>, sourcePage: Page<*>): Page<T> =
        sourcePage.map { source -> super.createProjection(projectionType.java, source!!) }

    fun <T : Any> createNullableProjectionPage(projectionType: KClass<T>, sourcePage: Page<*>): Page<T?> =
        sourcePage.map { source -> super.createNullableProjection(projectionType.java, source) }

}
