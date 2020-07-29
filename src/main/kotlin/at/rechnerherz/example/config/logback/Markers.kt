package at.rechnerherz.example.config.logback

import org.slf4j.Marker
import org.slf4j.MarkerFactory

/**
 * Marker used in logback-smtp-appender.xml to avoid sending a mail, ignoring the log level.
 */
val NO_MAIL: Marker = MarkerFactory.getMarker("NO_MAIL")

/**
 * Marker used in logback-smtp-appender.xml to force sending a mail, ignoring the log level.
 */
val SEND_MAIL: Marker = MarkerFactory.getMarker("SEND_MAIL")

/**
 * Marker used in logback-slack-appender.xml to post a message to slack.
 */
val POST_TO_SLACK: Marker = MarkerFactory.getMarker("POST_TO_SLACK")
