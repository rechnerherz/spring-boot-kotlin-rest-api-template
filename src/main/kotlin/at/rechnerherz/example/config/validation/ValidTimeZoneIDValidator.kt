package at.rechnerherz.example.config.validation

import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * [ConstraintValidator] for [ValidTimeZoneID].
 *
 * Registered under `META-INF/services/javax.validation.ConstraintValidator`.
 */
class ValidTimeZoneIDValidator : ConstraintValidator<ValidTimeZoneID, String> {

    override fun isValid(string: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean =
        string.isNullOrBlank() || TimeZone.getAvailableIDs().contains(string)
}
