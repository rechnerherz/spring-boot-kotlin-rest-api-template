package at.rechnerherz.example.domain.account.verification.registration

import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.domain.account.contact.ContactData
import at.rechnerherz.example.domain.account.contact.PersonalData
import at.rechnerherz.example.domain.account.patient.Customer
import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.mail.MailService
import at.rechnerherz.example.thymeleaf.ThymeleafService
import at.rechnerherz.example.util.buildURL
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class RegistrationService(
    private val baseProperties: BaseProperties,
    private val registrationTokenRepository: RegistrationTokenRepository,
    private val accountRepository: AccountRepository,
    private val mailService: MailService,
    private val thymeleafService: ThymeleafService
) {

    private val log = KotlinLogging.logger {}

    @Transactional(readOnly = false)
    fun registration(
        locale: Locale,
        username: String,
        password: String,
        personalData: PersonalData = PersonalData(),
        contactData: ContactData = ContactData()
    ): Account? {

        val account = accountRepository.findByUsername(username)

        if (account != null)
            return null

        var newAccount = Customer(username, password, personalData, contactData)
        newAccount.registrationSubmitted = Instant.now()
        newAccount.active = false

        newAccount = accountRepository.internalSave(newAccount)

        sendRegistrationConfirmationMail(newAccount, locale)

        return newAccount
    }

    @Transactional(readOnly = false)
    fun createRegistrationToken(receiver: Account): RegistrationToken {
        registrationTokenRepository.internalDeleteAll(registrationTokenRepository.findByAccount(receiver))
        return registrationTokenRepository.internalSave(RegistrationToken(receiver))
    }

    @Transactional(readOnly = false)
    fun sendRegistrationConfirmationMail(receiver: Account, locale: Locale) {

        val registrationToken = createRegistrationToken(receiver)
        val registrationConfirmationURL = buildURL(
            baseProperties.frontendUrl,
            "/account/register",
            "token" to registrationToken.token.toString()
        )

        log.debug { registrationConfirmationURL }

        val html = thymeleafService.processTemplate(
            locale,
            "/templates/account/registration/registration_confirmation_mail.html",
            "receiver" to receiver,
            "registrationConfirmationURL" to registrationConfirmationURL
        )

        mailService.sendMailFromHtmlTemplate(
            html,
            mailService.fromAddress,
            receiver.mailAddress
        )
    }

}
