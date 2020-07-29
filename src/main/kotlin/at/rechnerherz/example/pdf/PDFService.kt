package at.rechnerherz.example.pdf

import at.rechnerherz.example.domain.base.BaseProperties
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * Render (X)HTML to PDF with flying-saucer xhtmlrenderer.
 *
 * [The Flying Saucer User's Guide](https://flyingsaucerproject.github.io/flyingsaucer/r8/guide/users-guide-R8.html)
 */
@Service
class PDFService(
    private val baseProperties: BaseProperties
) {

    /**
     * Render HTML to PDF to an output stream.
     */
    fun htmlToPDFOutputStream(html: String, outputStream: OutputStream) {
        val renderer = ITextRenderer()
        renderer.sharedContext.userAgentCallback =
            CustomUserAgent(renderer.sharedContext, renderer.outputDevice)
        renderer.sharedContext.fontResolver = CustomFontResolver(renderer.sharedContext)
        renderer.setDocumentFromString(html, baseProperties.directory)
        renderer.layout()
        renderer.createPDF(outputStream)
    }

    /**
     * Render HTML to PDF as byte array.
     */
    fun htmlToPDFByteArray(html: String): ByteArray =
        ByteArrayOutputStream().use { outputStream ->
            htmlToPDFOutputStream(html, outputStream)
            outputStream.toByteArray()
        }

    /**
     * Render HTML to PDF as [ByteArrayResource].
     */
    fun htmlToPDFResource(html: String): ByteArrayResource =
        ByteArrayResource(htmlToPDFByteArray(html))


}
