package at.rechnerherz.example.config

import at.rechnerherz.example.domain.base.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import javax.persistence.EntityManagerFactory

/**
 * Spring Data REST configuration.
 *
 * [Configuring Spring Data REST](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#getting-started.configuration)
 */
@Configuration
class RestConfig {

    /**
     * A [KProjectionFactory] that can be used to manually create projections.
     */
    @Bean
    fun projectionFactory(): KProjectionFactory =
        KProjectionFactory()

    /**
     * Customize the REST config with a [RepositoryRestConfigurer].
     */
    @Bean
    fun repositoryRestConfigurer(entityManagerFactory: EntityManagerFactory): RepositoryRestConfigurer =
        object : RepositoryRestConfigurer {

            /**
             * - Only export repositories annotated with @RepositoryRestResource and methods annotated with @RestResource.
             * - Expose the ids of all entity classes.
             * - Register the [IdProjection] for all entity classes.
             * - Register the [IdNameProjection] for all entity classes with a name property.
             */
            override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {

                // Disable default exposure
                config.disableDefaultExposure()

                val entitiesWithId: List<Class<out Any>> = entityManagerFactory.metamodel.entities
                        .filter { entityType -> entityType.attributes.any { it.name == "id" } }
                        .map { it.javaType }

                if (entitiesWithId.isNotEmpty()) {
                    val entitiesWithIdArray = entitiesWithId.toTypedArray()

                    // Expose ids
                    config.exposeIdsFor(*entitiesWithIdArray)

                    // Register IdProjection
                    config.projectionConfiguration.addProjection(IdProjection::class.java, ID_PROJECTION, *entitiesWithIdArray)

                    // Register IdNameProjection
                    val entitiesWithName = entityManagerFactory.metamodel.entities
                        .filter { entityType -> entityType.attributes.any { it.name == "id" } && entityType.attributes.any { it.name == "name" } }
                        .map { it.javaType }

                    if (entitiesWithName.isNotEmpty())
                        config.projectionConfiguration.addProjection(IdNameProjection::class.java, ID_NAME_PROJECTION, *entitiesWithName.toTypedArray())
                }
            }
        }
}
