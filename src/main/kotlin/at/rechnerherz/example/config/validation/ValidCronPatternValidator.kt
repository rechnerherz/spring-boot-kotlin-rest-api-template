package at.rechnerherz.example.config.validation

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.support.CronSequenceGenerator
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * [ConstraintValidator] for [ValidCronPattern].
 *
 * Registered under `META-INF/services/javax.validation.ConstraintValidator`.
 */
class ValidCronPatternValidator : ConstraintValidator<ValidCronPattern, String> {

    override fun isValid(string: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean =
        string == null || string == Scheduled.CRON_DISABLED || CronSequenceGenerator.isValidExpression(string)
}
