package at.rechnerherz.example.domain.document

import org.springframework.http.MediaType

/**
 * Enum of supported document types.
 */
enum class DocumentType(val mimeType: String) {

    JPG(MediaType.IMAGE_JPEG_VALUE),
    PNG(MediaType.IMAGE_PNG_VALUE),
    PDF(MediaType.APPLICATION_PDF_VALUE);

    fun extension(): String =
        name.toLowerCase()

    fun isImage(): Boolean =
        MediaType.parseMediaType(mimeType).type == "image"

    companion object {
        fun fromContentType(contentType: String?): DocumentType =
            when (contentType) {
                MediaType.IMAGE_JPEG_VALUE -> JPG
                MediaType.IMAGE_PNG_VALUE -> PNG
                MediaType.APPLICATION_PDF_VALUE -> PDF
                else -> throw IllegalArgumentException("Unsupported content type")
            }

        fun isAccepted(contentType: String?): Boolean =
            values().map { it.mimeType }.contains(contentType)
    }

}
