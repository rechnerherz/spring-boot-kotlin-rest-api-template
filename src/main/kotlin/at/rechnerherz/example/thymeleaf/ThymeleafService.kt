package at.rechnerherz.example.thymeleaf

import at.rechnerherz.example.config.aop.NoProfiling
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext
import java.util.*

/**
 * Service to process Thymeleaf templates to HTML.
 *
 * [Using Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
 */
@Service
class ThymeleafService(
    private val templateEngine: TemplateEngine,
    private val applicationContext: ApplicationContext
) {

    @NoProfiling
    fun processTemplate(locale: Locale, template: String, vararg variables: Pair<String, Any?>): String {
        val context = Context(locale)

        // Set the Thymeleaf evaluation context to allow access to Spring beans with @beanName in SpEL expressions
        context.setVariable(
            ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME,
            ThymeleafEvaluationContext(applicationContext, null)
        )

        // Set additional variables
        variables.forEach { (key, value) -> context.setVariable(key, value) }

        return templateEngine.process(template, context)
    }
}
