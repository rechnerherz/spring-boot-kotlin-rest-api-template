package at.rechnerherz.example.domain.account.verification.password

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.mail.MailService
import at.rechnerherz.example.thymeleaf.ThymeleafService
import at.rechnerherz.example.util.buildURL
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PasswordResetService(
    private val baseProperties: BaseProperties,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val mailService: MailService,
    private val thymeleafService: ThymeleafService
) {

    private val log = KotlinLogging.logger {}

    @Transactional(readOnly = false)
    fun createPasswordResetToken(account: Account): PasswordResetToken {
        passwordResetTokenRepository.internalDeleteAll(passwordResetTokenRepository.findByAccount(account))
        passwordResetTokenRepository.flush()
        return passwordResetTokenRepository.internalSave(PasswordResetToken(account))
    }

    @Transactional(readOnly = false)
    fun sendPasswordResetMail(receiver: Account, locale: Locale) {

        val passwordResetToken = createPasswordResetToken(receiver)
        val passwordResetURL = buildURL(
            baseProperties.frontendUrl,
            "/account/reset",
            "token" to passwordResetToken.token.toString()
        )

        log.debug { passwordResetURL }

        val html = thymeleafService.processTemplate(
            locale,
            "/templates/account/password/password_reset_mail.html",
            "receiver" to receiver,
            "passwordResetURL" to passwordResetURL
        )

        mailService.sendMailFromHtmlTemplate(
            html,
            mailService.fromAddress,
            receiver.mailAddress
        )
    }

}
