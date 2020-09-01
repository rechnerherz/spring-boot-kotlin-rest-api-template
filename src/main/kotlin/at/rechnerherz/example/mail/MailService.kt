package at.rechnerherz.example.mail

import at.rechnerherz.aoprofiling.NoProfiling
import at.rechnerherz.example.util.toFormattedPlainText
import at.rechnerherz.example.util.wrapHTMLBody
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.springframework.core.io.InputStreamSource
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.Semaphore
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.concurrent.schedule

/**
 * Service for sending emails.
 *
 * [Sending Email](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-email.html)
 * [Email](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#mail)
 */
@Service
@NoProfiling
class MailService(
    private val javaMailSender: JavaMailSender,
    private val mailProperties: MailProperties
) {

    private val log = KotlinLogging.logger {}

    /**
     * A semaphore to limit concurrent sending.
     *
     * At most one thread can send at a time, waiting threads are handling in a fair (FIFO) matter.
     */
    private val semaphore = Semaphore(1, true)

    val debugAddress = MailAddress(mailProperties.debugAddress, mailProperties.debugPersonal)

    val fromAddress = MailAddress(mailProperties.fromAddress, mailProperties.fromPersonal)

    /**
     * Send a mail from an HTML template.
     *
     * The HTML title is used as mail subject.
     */
    @Async
    fun sendMailFromHtmlTemplate(
        html: String,
        from: MailAddress,
        to: MailAddress,
        replyTo: MailAddress? = null,
        bcc: MailAddress? = null,
        vararg attachments: MailAttachment
    ) {
        val document = Jsoup.parse(html)
        val subject = document.head().getElementsByTag("title").first().text()
        val htmlBody = document.body().html()
        sendMail(subject, htmlBody, from, to, replyTo, bcc, *attachments)
    }

    /**
     * Send a mail as mixed HTML/plain text.
     */
    @Async
    fun sendMail(
        subject: String,
        htmlBody: String,
        from: MailAddress,
        to: MailAddress,
        replyTo: MailAddress? = null,
        bcc: MailAddress? = null,
        vararg attachments: MailAttachment
    ) {
        val details =
            "\"$subject\" from ${from.address} to ${to.address}" + if (mailProperties.logBody) "\n$htmlBody" else ""

        if (mailProperties.enabled) {

            try {
                val mimeMessage = javaMailSender.createMimeMessage()
                val mimeMessageHelper = MimeMessageHelper(mimeMessage, true)
                mimeMessage.setFrom(from.toInternetAddress())

                // If debugMode is enabled, send mails to debugAddress instead of the real receiver
                val toOrDebug = if (mailProperties.debugMode) debugAddress else to
                mimeMessageHelper.setTo(toOrDebug.toInternetAddress())
                mimeMessageHelper.setSubject(subject)

                val htmlText = htmlBody.wrapHTMLBody(subject)
                val plainText = htmlText.toFormattedPlainText()
                mimeMessageHelper.setText(plainText, htmlText)

                if (replyTo != null)
                    mimeMessageHelper.setReplyTo(replyTo.toInternetAddress())

                // If debugMode is enabled, don't send to BCCs
                if (!mailProperties.debugMode) {

                    val bccList = mutableListOf<InternetAddress>()

                    if (bcc != null)
                        bccList.add(bcc.toInternetAddress())

                    // If debugBcc is enabled, add the debugAddress as BCC
                    if (mailProperties.debugBcc)
                        bccList.add(debugAddress.toInternetAddress())

                    mimeMessageHelper.setBcc(bccList.toTypedArray())
                }

                attachments.forEach { (name, source) -> mimeMessageHelper.addAttachment(name, source) }

                waitSendAndRetry(mimeMessage, details, javaMailSender)

            } catch (e: MessagingException) {
                log.error(e) { "Failed to create mail $details" }
            }

        } else {
            log.info { "Skip sending mail $details" }
        }
    }

    /**
     * Wait if necessary, then send the mail, and retry if a connection error happens.
     */
    private fun waitSendAndRetry(
        mimeMessage: MimeMessage,
        details: String,
        mailSender: JavaMailSender,
        retryCount: Int = 0
    ) {

        try {

            // Acquire a permit: blocks the current thread if none available
            semaphore.acquire()

            // Send the mail
            log.info { "Sending mail $details" }
            mailSender.send(mimeMessage)

        } catch (e: MailException) {

            if (e.cause is com.sun.mail.util.MailConnectException) {

                if (retryCount < mailProperties.maxRetries) {
                    log.warn(e) { "Connection error after $retryCount retries. Try again to send mail $details" }

                    // Try again (in a different thread) after retryDelay
                    Timer("MailServiceRetry").schedule(mailProperties.retryDelay.toMillis()) {
                        waitSendAndRetry(mimeMessage, details, mailSender, retryCount + 1)
                    }
                } else
                    log.error(e) { "Connection error after $retryCount retries. Giving up trying to send mail $details" }

            } else
                log.error(e) { "Failed to send mail $details" }

        } finally {

            // Release the permit after sendDelay millis
            Timer("MailServiceRelease").schedule(mailProperties.sendDelay.toMillis()) {
                semaphore.release()
            }
        }
    }

    fun MailAddress.toInternetAddress(): InternetAddress {
        val addressOrDebug = if (address.isBlank()) {
            log.warn("Blank mail address. Using debug address instead.")
            debugAddress.address
        } else address

        return if (personal.isNullOrBlank())
            InternetAddress(addressOrDebug)
        else
            InternetAddress(addressOrDebug, personal)
    }
}

data class MailAddress(
    val address: String,
    val personal: String? = null
)

data class MailAttachment(
    val name: String,
    val source: InputStreamSource
)
