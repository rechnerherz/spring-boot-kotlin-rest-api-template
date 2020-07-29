package at.rechnerherz.example.config.validation

import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * [ConstraintValidator] for [ValidRegex].
 *
 * Registered under `META-INF/services/javax.validation.ConstraintValidator`.
 */
class ValidRegexValidator : ConstraintValidator<ValidRegex, String> {

    override fun isValid(string: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean =
        string == null || try {
            Pattern.compile(string)
            true
        } catch (e: PatternSyntaxException) {
            false
        }

}
