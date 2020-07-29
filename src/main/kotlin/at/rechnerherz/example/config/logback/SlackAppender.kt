package at.rechnerherz.example.config.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.UnsynchronizedAppenderBase
import ch.qos.logback.core.boolex.EvaluationException
import ch.qos.logback.core.boolex.EventEvaluator
import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackMessage

/**
 * A Logback [ch.qos.logback.core.Appender] to post messages to Slack using the [SlackApi].
 */
class SlackAppender : UnsynchronizedAppenderBase<ILoggingEvent>() {

    var evaluator: EventEvaluator<ILoggingEvent>? = null
    var webhookURL: String? = null
    var channel: String? = null
    var username: String? = null
    var icon: String? = null

    private lateinit var slackApi: SlackApi

    override fun start() {
        when {
            evaluator == null -> addError("No evaluator set for the appender '$name'.")
            webhookURL == null -> addError("No webhookURL set for the appender '$name'.")
            else -> {
                slackApi = SlackApi(webhookURL)
                super.start()
            }
        }
    }

    override fun append(event: ILoggingEvent) {
        if (isStarted) {
            try {
                if (evaluator!!.evaluate(event)) {
                    val message = SlackMessage(channel, username, event.formattedMessage).setIcon(icon)
                    slackApi.call(message)
                }
            } catch (e: EvaluationException) {
                addError("Exception in appender '$name'.", e)
            }
        }
    }
}
