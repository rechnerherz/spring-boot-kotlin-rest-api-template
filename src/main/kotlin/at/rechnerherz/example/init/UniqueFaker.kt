package at.rechnerherz.example.init

import com.github.javafaker.*
import java.util.*
import kotlin.collections.HashMap

/*
 * Extension functions for Faker to generate unique values.
 */

fun Name.uniqueLastName(): String =
    uniqueStrings.unique("name.lastName", ::lastName)

fun Company.uniqueName(): String =
    uniqueStrings.unique("company.name", ::name)

fun Commerce.uniqueProductName(): String =
    uniqueStrings.unique("commerce.productName", ::productName)

fun Music.uniqueInstrument(): String =
    uniqueStrings.unique("music.instrument", ::instrument)

fun Hipster.uniqueWord(): String =
    uniqueStrings.unique("hipster.word", ::word)

fun Pokemon.uniqueName(): String =
    uniqueStrings.unique("pokemon.name", {
        name()
            .replace("♀", "")
            .replace("♂", "")
    })

fun Commerce.uniqueColor(): String =
    uniqueStrings.unique("commerce.color", ::color)

fun Lorem.uniqueWord(): String =
    uniqueStrings.unique("lorem.word", ::word)

fun resetUnique() {
    uniqueStrings.clear()
}

fun resetUnique(key: String) {
    uniqueStrings[key]?.clear()
}

private val uniqueStrings: UniqueStrings = UniqueStrings(HashMap())

class UniqueStrings(
    private val valuesMap: MutableMap<String, MutableSet<String>>
) : MutableMap<String, MutableSet<String>> by valuesMap {

    fun unique(
        key: String,
        provider: () -> String,
        comparator: Comparator<String>? = null,
        maxAttempts: Int = 100
    ): String {
        val values: MutableSet<String> = valuesMap.computeIfAbsent(key) { TreeSet(comparator) }
        var value: String
        for (i in 0 until maxAttempts) {
            value = provider()
            if (!values.contains(value)) {
                values.add(value)
                return value
            }
        }
        throw IllegalStateException("Could not obtain a unique value after $maxAttempts attempts.")
    }

}
