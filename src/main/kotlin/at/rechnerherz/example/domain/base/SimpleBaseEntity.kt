package at.rechnerherz.example.domain.base

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * A [BaseEntity] with a simple generated primary key.
 *
 * Entities should inherit from this class, unless they use a shared or composite primary key.
 */
@MappedSuperclass
abstract class SimpleBaseEntity : BaseEntity() {

    /**
     * The primary key.
     *
     * The [Id] annotation is on the property: Hibernate will use property access for all attributes.
     *
     * Generated using the [GenerationType.IDENTITY] strategy (auto_increment in MySQL).
     * Null if the entity is new, i.e. has not been persisted yet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    override var id: Long? = null

}
