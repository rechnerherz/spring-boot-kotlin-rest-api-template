package at.rechnerherz.example.util

/** Access a private field with Java reflection. Only use for debugging or when there's no other option. */
fun getPrivateField(any: Any, fieldName: String): Any? =
    any.javaClass.getDeclaredField(fieldName).let {
        it.isAccessible = true
        it.get(any)
    }
