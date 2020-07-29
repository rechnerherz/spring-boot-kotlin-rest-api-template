package at.rechnerherz.example.pdf

import at.rechnerherz.example.util.getPrivateField
import org.xhtmlrenderer.css.constants.CSSName
import org.xhtmlrenderer.css.sheet.FontFaceRule
import org.xhtmlrenderer.css.sheet.PropertyDeclaration
import org.xhtmlrenderer.css.sheet.Ruleset
import org.xhtmlrenderer.layout.SharedContext
import org.xhtmlrenderer.pdf.ITextFontResolver

/**
 * A font resolver that only imports TTFs (other fonts are not supported anyway).
 *
 * Very hacky, because the _ruleset field in FontFaceRule is private.
 */
class CustomFontResolver(sharedContext: SharedContext) : ITextFontResolver(sharedContext) {

    @Suppress("UNCHECKED_CAST")
    override fun importFontFaces(fontFaces: MutableList<Any?>) {
        val ttfFontFaces = fontFaces.filter { fontFaceRule ->
            val ruleset = getPrivateField((fontFaceRule as FontFaceRule), "_ruleset") as Ruleset
            val props = ruleset.propertyDeclarations as List<PropertyDeclaration>
            props.any { it.cssName == CSSName.SRC && it.value.cssText.contains(".ttf") }
        }
        super.importFontFaces(ttfFontFaces)
    }

}
