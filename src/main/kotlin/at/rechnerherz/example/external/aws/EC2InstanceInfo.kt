package at.rechnerherz.example.external.aws

import at.rechnerherz.example.config.PRODUCTION
import at.rechnerherz.example.config.STAGING
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Obtain and log some info about the EC2 instance.
 *
 * [Cloud Environment](https://cloud.spring.io/spring-cloud-static/spring-cloud-aws/2.0.1.RELEASE/single/spring-cloud-aws.html#_cloud_environment)
 */
@Component
@Profile(STAGING, PRODUCTION)
data class EC2InstanceInfo(

    @Value("\${ami-id:null}")
    val amiId: String?,

    @Value("\${hostname:null}")
    val hostname: String?,

    @Value("\${instance-type:null}")
    val instanceType: String?,

    @Value("\${services/domain:null}")
    val serviceDomain: String?

) : ApplicationRunner {

    private val log = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments?) {
        log.debug { this }
    }

}
