package at.rechnerherz.example.util

import org.springframework.core.io.Resource
import org.springframework.core.io.WritableResource
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import javax.servlet.http.HttpServletResponse

/**
 * Copy an [InputStream] to the file system.
 */
fun InputStream.copyToFileSystem(destination: Path) {
    use { inputStream -> Files.copy(inputStream, destination) }
}

/**
 * Copy an [InputStream] to a [WritableResource].
 */
fun InputStream.copyToResource(destination: WritableResource) {
    use { inputStream ->
        destination.outputStream.use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
}

/**
 * Copy a [Resource] to a [WritableResource].
 */
fun Resource.copyToResource(destination: WritableResource) {
    inputStream.copyToResource(destination)
}

/**
 * Read an [InputStream] using a buffered reader and return it as a string.
 */
fun InputStream.readToString(charset: Charset = Charsets.UTF_8): String =
    bufferedReader(charset).use { bufferedReader -> bufferedReader.readText() }

/**
 * Write a string to an [OutputStream].
 */
fun OutputStream.writeString(string: String, charset: Charset = Charsets.UTF_8) {
    use { outputStream -> outputStream.write(string.toByteArray(charset)) }
}

/**
 * Copy an [InputStream] to the [HttpServletResponse] response outputStream, set the response contentType, and commit (flush) the response.
 */
fun InputStream.copyToResponse(response: HttpServletResponse, contentType: String) {
    response.contentType = contentType
    copyTo(response.outputStream)
    response.flushBuffer()
}
