package at.rechnerherz.example.config.aop

import com.google.common.base.Ascii
import com.google.common.base.MoreObjects
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.aop.framework.Advised
import org.springframework.boot.ApplicationArguments
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.coroutines.Continuation

fun JoinPoint.targetAndMethodName(): String =
    getTargetName() + "." + signature.name

fun JoinPoint.getTargetName(): String {
    val target: Any = target ?: return "[no target]"

    if (target is Advised && target.javaClass.canonicalName.contains("\$Proxy")) {
        try {
            val name: String = target.targetSource.targetClass?.simpleName ?: ""
            if (name == "SimpleJpaRepository" || name.endsWith("RepositoryImpl"))
                target.javaClass.interfaces.firstOrNull()?.simpleName?.let { return it }
            return name
        } catch (e: Exception) {
            return "[target can't be resolved]"
        }
    }

    return target.javaClass.simpleName
}

fun JoinPoint.signature(
    prettyPrint: Boolean = false,
    truncate: Int? = null
): String {
    val joinPointArgs: Array<Any?> = args
    val multipleArgs: Boolean = joinPointArgs.size > 1
    val separator: String = if (prettyPrint && multipleArgs) ",\n\t" else ", "
    val msg = StringBuilder()
    if (prettyPrint)
        msg.append("\n")
    msg.append(targetAndMethodName())
    if (prettyPrint && multipleArgs)
        msg.append("\n")
    msg.append("(")
    if (prettyPrint && multipleArgs)
        msg.append("\n\t")
    msg.append(joinPointArgs.joinToString(separator = separator) { cleanToString(it, prettyPrint, truncate) })
    if (prettyPrint && multipleArgs)
        msg.append("\n")
    msg.append(")")
    if (prettyPrint)
        msg.append("\n")
    return msg.toString()
}

fun JoinPoint.returnValueToString(
    returnValue: Any?,
    prettyPrint: Boolean,
    truncate: Int?
): String =
    if (hasVoidReturnType())
        "void"
    else
        try {
            cleanToString(returnValue, prettyPrint, truncate)
        } catch (e: Exception) {
            "[" + e.message + "]"
        }

fun JoinPoint.hasVoidReturnType(): Boolean =
    signature is MethodSignature && (signature as MethodSignature).returnType == Void.TYPE

fun cleanToString(
    any: Any?,
    prettyPrint: Boolean = false,
    truncate: Int? = null
): String {
    val string: String = when (any) {
        is Array<*> -> any.cleanToString()
        is Collection<*> -> any.cleanToString()
        is ApplicationArguments -> any.sourceArgs.joinToString()
        is MultipartFile -> any.cleanToString()
        is Continuation<*> -> "[coroutine]"
        is HttpServletRequest -> any.cleanToString()
        is HttpServletResponse -> any.cleanToString()
        else -> any.toString()
    }
    return if (prettyPrint)
        string
    else {
        val index = string.indexOf('\n')
        val firstLine = if (index != -1)
            string.substring(0, index) + "..."
        else
            string
        if (truncate != null)
            Ascii.truncate(firstLine, truncate, "...")
        else
            firstLine
    }
}

private fun Array<*>.cleanToString(): String {
    return if (isEmpty())
        "empty Array"
    else {
        val content = get(0)
        if (size == 1) {
            "Array containing ${cleanToString(content)}"
        } else {
            val contentClass = content?.javaClass?.simpleName ?: ""
            "Array containing $size $contentClass"
        }
    }
}

private fun Collection<*>.cleanToString(): String {
    val collectionClass = this::class.simpleName ?: "anonymous collection"
    return if (isEmpty())
        "empty $collectionClass"
    else {
        val content = iterator().next()
        if (size == 1) {
            "$collectionClass containing ${cleanToString(content)}"
        } else {
            val contentClass = content?.javaClass?.simpleName ?: ""
            "$collectionClass containing $size $contentClass"
        }
    }
}

private fun MultipartFile.cleanToString(): String =
    MoreObjects
        .toStringHelper(MultipartFile::class.java.simpleName)
        .add("name", name)
        .toString()

private fun HttpServletRequest.cleanToString(): String =
    MoreObjects
        .toStringHelper(this)
        .add("method", method)
        .add("requestURI", requestURI)
        .toString()

private fun HttpServletResponse.cleanToString(): String =
    MoreObjects.toStringHelper(this)
        .add("status", status)
        .toString()
