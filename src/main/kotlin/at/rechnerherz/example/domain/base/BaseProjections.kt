package at.rechnerherz.example.domain.base

import org.springframework.data.rest.core.config.Projection
import java.time.Instant

/** Default projection. */
const val DEFAULT_PROJECTION = "default"

/** Projection exposing just the id. */
const val ID_PROJECTION = "id"

/** Projection exposing just the id and name. */
const val ID_NAME_PROJECTION = "id-name"

/** Projection exposing the entity and all related entities (excluding the company for entities that are fetched by the company id). */
const val FULL_PROJECTION = "full"

/** Projection exposing the entity and all nested related entities and calculated properties for the details view. */
const val DETAILS_PROJECTION = "details"

/** Projection for the main search component. */
const val SEARCH_PROJECTION = "search"

/** Projection for the booking process. */
const val BOOKING_PROJECTION = "booking"

/** Projection with all additional details for booking process. */
const val BOOKING_DETAILS_PROJECTION = "booking-details"

const val COMPANY_PROJECTION = "company"

/** Interface for projections exposing fields from [BaseEntity] (except [BaseEntity.createdBy] and [BaseEntity.modifiedBy]). */
interface BaseProjection {

    val id: Long?
    val version: Long
    val createdAt: Instant
    val modifiedAt: Instant
    val active: Boolean
    val className: String
    val identifier: String

}

/** Projection exposing only the id. Registered for all entity types in [at.rechnerherz.example.config.RestConfig]. */
@Projection(name = ID_PROJECTION, types = [])
interface IdProjection {

    val id: Long?
}

/** Projection exposing only the id and name. Registered for all entity types with a name attribute in [at.rechnerherz.example.config.RestConfig]. */
@Projection(name = ID_NAME_PROJECTION, types = [])
interface IdNameProjection {

    val id: Long?
    val name: String
}
