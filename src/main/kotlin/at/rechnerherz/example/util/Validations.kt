package at.rechnerherz.example.util

import javax.validation.ConstraintViolationException
import javax.validation.Validator

/**
 * Calls [Validator.validate] and returns whether the entity is valid.
 */
fun <T> Validator.isValid(entity: T): Boolean =
    validate(entity).isEmpty()

/**
 * Calls [Validator.validate] and throws a [ConstraintViolationException] if the entity is not valid.
 */
fun <T> Validator.throwIfInvalid(entity: T) {
    val constraintViolations = validate(entity)
    if (constraintViolations.isNotEmpty())
        throw ConstraintViolationException(constraintViolations)
}
