package at.rechnerherz.example.util

import kotlin.reflect.KClass

fun KClass<*>.packageName(): String =
    java.`package`.name
