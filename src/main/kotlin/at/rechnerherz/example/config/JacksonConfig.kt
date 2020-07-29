package at.rechnerherz.example.config

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
//import org.zalando.jackson.datatype.money.MoneyModule
import org.zalando.problem.ProblemModule
import org.zalando.problem.violations.ConstraintViolationProblemModule

/**
 * Jackson configuration.
 *
 * Jackson module beans are automatically registered with the [org.springframework.http.converter.json.Jackson2ObjectMapperBuilder].
 *
 * [Customize the Jackson ObjectMapper](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-spring-mvc.html#howto-customize-the-jackson-objectmapper)
 */
@Configuration
class JacksonConfig {

    /**
     * Register the [KotlinModule].
     */
    @Bean
    fun kotlinModule(): Module =
        KotlinModule()

//    /**
//     * Register the [MoneyModule].
//     */
//    @Bean
//    fun moneyModule(): Module =
//        MoneyModule()

    /**
     * Register the [Hibernate5Module] with [Hibernate5Module.Feature.FORCE_LAZY_LOADING].
     */
    @Bean
    fun hibernateModule(): Module =
        Hibernate5Module().apply {
            enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING)
        }

    /**
     * Register the [ProblemModule].
     */
    @Bean
    fun problemModule(): Module =
        ProblemModule()

    /**
     * Register the [ConstraintViolationProblemModule].
     */
    @Bean
    fun constraintViolationProblemModule(): Module =
        ConstraintViolationProblemModule()
}
