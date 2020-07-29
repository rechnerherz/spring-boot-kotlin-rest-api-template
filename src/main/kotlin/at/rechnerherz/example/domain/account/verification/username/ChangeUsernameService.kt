package at.rechnerherz.example.domain.account.verification.username

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.mail.MailAddress
import at.rechnerherz.example.mail.MailService
import at.rechnerherz.example.thymeleaf.ThymeleafService
import at.rechnerherz.example.util.buildURL
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChangeUsernameService(
    private val baseProperties: BaseProperties,
    private val changeUsernameTokenRepository: ChangeUsernameTokenRepository,
    private val mailService: MailService,
    private val thymeleafService: ThymeleafService
) {

    private val log = KotlinLogging.logger {}

    @Transactional(readOnly = false)
    fun createChangeUsernameToken(account: Account, username: String): ChangeUsernameToken {
        changeUsernameTokenRepository.internalDeleteAll(changeUsernameTokenRepository.findByAccount(account))
        changeUsernameTokenRepository.flush()
        return changeUsernameTokenRepository.internalSave(ChangeUsernameToken(account, username))
    }

    @Transactional(readOnly = false)
    fun sendChangeUsernameMail(receiver: Account, username: String, locale: Locale) {
        val changeUsernameToken = createChangeUsernameToken(receiver, username)
        val changeUsernameURL = buildURL(
            baseProperties.frontendUrl,
            "/account/change-username",
            "token" to changeUsernameToken.token.toString()
        )

        log.debug { changeUsernameURL }

        val html = thymeleafService.processTemplate(
            locale,
            "/templates/account/username/change_username_mail.html",
            "receiver" to receiver,
            "changeUsernameURL" to changeUsernameURL
        )

        mailService.sendMailFromHtmlTemplate(
            html,
            mailService.fromAddress,
            MailAddress(username, receiver.personalData.fullName)
        )
    }

}
