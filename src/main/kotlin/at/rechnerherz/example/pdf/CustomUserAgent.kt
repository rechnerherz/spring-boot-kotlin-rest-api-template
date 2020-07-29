package at.rechnerherz.example.pdf

import at.rechnerherz.example.util.appendPath
import org.xhtmlrenderer.layout.SharedContext
import org.xhtmlrenderer.pdf.ITextOutputDevice
import org.xhtmlrenderer.pdf.ITextUserAgent
import java.net.URI

/**
 * A [ITextUserAgent] that properly resolves relative and absolute URIs.
 */
class CustomUserAgent(
    sharedContext: SharedContext,
    outputDevice: ITextOutputDevice
) : ITextUserAgent(outputDevice) {

    init {
        setSharedContext(sharedContext)
    }

    /**
     * Keep absolute URLs and resolve all other URLs relative to the base URL.
     */
    override fun resolveURI(uri: String): String =
        if (URI(uri).isAbsolute) uri
        else baseURL.appendPath(uri)

}
