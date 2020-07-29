package at.rechnerherz.example.domain.base.deletable

import at.rechnerherz.example.domain.base.exception.TranslatableException

/**
 * An exception that is thrown when a user tries to delete an entity which is referenced by other entities.
 */
class EntityNotDeletableException(
    msg: String,
    key: String,
    options: Map<String, String> = emptyMap(),
    cause: Throwable? = null
) :
    TranslatableException(msg, key, options, cause)

