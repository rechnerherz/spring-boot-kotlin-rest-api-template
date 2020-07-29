package at.rechnerherz.example.external.aws

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient

@Configuration
class SESConfig(
    private val sesProperties: SESProperties
) {

    @Bean
    fun sesClient(): SesClient =
        SesClient.builder()
            .region(Region.of(sesProperties.regionName))
            .build()

}
