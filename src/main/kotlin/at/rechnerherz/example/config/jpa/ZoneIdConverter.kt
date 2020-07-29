package at.rechnerherz.example.config.jpa

import java.time.ZoneId
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * A JPA [AttributeConverter] to persist [ZoneId] values as [String].
 *
 * [JPA 2.1 AttributeConverters](http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#basic-jpa-convert)
 */
@Converter(autoApply = true)
class ZoneIdConverter : AttributeConverter<ZoneId, String> {

    override fun convertToDatabaseColumn(attribute: ZoneId?): String? =
        attribute?.id

    override fun convertToEntityAttribute(dbData: String?): ZoneId? =
        dbData?.let { ZoneId.of(dbData) }
}
