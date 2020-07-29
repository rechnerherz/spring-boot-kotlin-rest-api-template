package at.rechnerherz.example.config.jpa

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * A JPA [AttributeConverter] to persist [ZonedDateTime] values as [String], using [DateTimeFormatter.ISO_ZONED_DATE_TIME].
 *
 * [JPA 2.1 AttributeConverters](http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#basic-jpa-convert)
 */
@Converter(autoApply = true)
class ZonedDateTimeConverter : AttributeConverter<ZonedDateTime, String> {

    override fun convertToDatabaseColumn(attribute: ZonedDateTime?): String? =
        attribute?.let { DateTimeFormatter.ISO_ZONED_DATE_TIME.format(attribute) }

    override fun convertToEntityAttribute(dbData: String?): ZonedDateTime? =
        dbData?.let { ZonedDateTime.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(it)) }

}
