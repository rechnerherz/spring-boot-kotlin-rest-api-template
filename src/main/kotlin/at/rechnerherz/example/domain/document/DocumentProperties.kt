package at.rechnerherz.example.domain.document

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Positive

@ConfigurationProperties(prefix = "at.rechnerherz.example.document")
@Validated
class DocumentProperties {

    /** The maximum size in pixels for uploaded images in either dimension. */
    @Positive
    var maxImageSize: Int = 2048

    /** Map of suffices to sizes for extra images size to generate. */
    var extraImageSizes: MutableMap<String, @Positive Int> = mutableMapOf(
        "lg" to 1024,
        "md" to 512,
        "sm" to 256
    )

}
