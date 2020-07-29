package at.rechnerherz.example.config.validation

import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * [ConstraintValidator] for [ValidLocale].
 *
 * Registered under `META-INF/services/javax.validation.ConstraintValidator`.
 */
class ValidLocaleValidator : ConstraintValidator<ValidLocale, String> {

    override fun isValid(string: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean =
        string.isNullOrBlank() || try {
            Locale.Builder().setLanguageTag(string)
            true
        } catch (e: IllformedLocaleException) {
            false
        }
}
