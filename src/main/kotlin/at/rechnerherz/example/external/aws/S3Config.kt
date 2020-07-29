package at.rechnerherz.example.external.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.cloud.aws.context.support.io.SimpleStorageProtocolResolverConfigurer
import org.springframework.cloud.aws.core.io.s3.SimpleStorageProtocolResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(
    private val s3Properties: S3Properties
) {

    companion object {
        const val S3_PROTOCOL_PREFIX = "s3://"
    }

    /**
     * Amazon S3 client using the [com.amazonaws.auth.DefaultAWSCredentialsProviderChain] to obtain credentials.
     */
    @Bean
    fun amazonS3(): AmazonS3 =
        AmazonS3ClientBuilder
            .standard()
            .withRegion(s3Properties.regionName)
            .build()

    @Bean
    fun simpleStorageProtocolResolver(amazonS3: AmazonS3): SimpleStorageProtocolResolver =
        SimpleStorageProtocolResolver(amazonS3)

    @Bean
    fun simpleStorageProtocolResolverConfigurer(simpleStorageProtocolResolver: SimpleStorageProtocolResolver): SimpleStorageProtocolResolverConfigurer =
        SimpleStorageProtocolResolverConfigurer(simpleStorageProtocolResolver)

}
