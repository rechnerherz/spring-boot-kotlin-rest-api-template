package at.rechnerherz.example.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

/**
 * Parse a JSON string to a map with string keys.
 */
fun parseJsonStringAsMap(string: String): Map<String, Any> =
    ObjectMapper().readValue(string, object : TypeReference<Map<String, Any>>() {})

/**
 * Serialize an object to pretty printed JSON with the Jackson ObjectMapper.
 */
fun ObjectMapper.prettyPrintJson(any: Any): String =
    copy()
        .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
        .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        .writerWithDefaultPrettyPrinter().writeValueAsString(any)

/**
 * Serialize an object to pretty printed JSON with the Jackson ObjectMapper.
 */
fun prettyPrintJson(any: Any): String =
    ObjectMapper().prettyPrintJson(any)

/**
 * Pretty print a JSON string with the Jackson ObjectMapper.
 */
fun ObjectMapper.prettyPrintJsonString(string: String): String =
    prettyPrintJson(readValue(string, Any::class.java))

/**
 * Pretty print a JSON string with the Jackson ObjectMapper.
 */
fun prettyPrintJsonString(string: String): String =
    ObjectMapper().prettyPrintJsonString(string)
