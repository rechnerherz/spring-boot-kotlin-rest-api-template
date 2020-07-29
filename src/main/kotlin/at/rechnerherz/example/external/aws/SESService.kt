package at.rechnerherz.example.external.aws

import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesRequest
import software.amazon.awssdk.services.ses.model.IdentityType
import software.amazon.awssdk.services.ses.model.ListIdentitiesRequest
import software.amazon.awssdk.services.ses.model.VerificationStatus

@Service
class SESService(
    val sesProperties: SESProperties,
    val sesClient: SesClient
) : ApplicationRunner {

    private val log = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments?) {
        if (sesProperties.testOnStartup) {
            try {
                testOnStartup()
            } catch (e: Exception) {
                log.error(e) { "Could not connect to SES service" }
            }
        }
    }

    private fun testOnStartup() {
        val enabled = sesClient.accountSendingEnabled.enabled()
        if (enabled)
            log.info { "SES account sending is enabled" }
        else
            log.error { "SES account sending is not enabled" }

        val sentLast24Hours = sesClient.sendQuota.sentLast24Hours().toInt()
        val max24HourSend = sesClient.sendQuota.max24HourSend().toInt()
        val maxSendRate = sesClient.sendQuota.maxSendRate().toInt()

        log.info { "SES quota: sent $sentLast24Hours / $max24HourSend in the last 2h. Max send rate: $maxSendRate / second." }

        getIdentities(IdentityType.EMAIL_ADDRESS).forEach { identity ->
            val verificationStatus = getIdentityVerificationStatus(identity)
            if (verificationStatus == VerificationStatus.SUCCESS)
                log.info { "SES identity '$identity' is verified" }
            else
                log.warn { "SES identity '$identity' is not verified: $verificationStatus" }
        }
    }

    fun getIdentities(identityType: IdentityType? = null): List<String> =
        sesClient
            .listIdentities(ListIdentitiesRequest
                .builder()
                .also { if (identityType != null) it.identityType(identityType) }
                .build())
            .identities()

    fun getIdentityVerificationStatus(identity: String): VerificationStatus? =
        sesClient
            .getIdentityVerificationAttributes(
                GetIdentityVerificationAttributesRequest
                    .builder()
                    .identities(identity)
                    .build()
            )
            .verificationAttributes()[identity]
            ?.verificationStatus()
}
