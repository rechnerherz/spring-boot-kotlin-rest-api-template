package at.rechnerherz.example.util

import java.io.PrintWriter
import java.io.StringWriter

/**
 * Prints this throwable and its backtrace to a string.
 */
fun Throwable.printStackTraceToString(): String =
    StringWriter().let {
        printStackTrace(PrintWriter(it))
        it.toString()
    }

