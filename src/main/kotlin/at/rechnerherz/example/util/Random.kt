package at.rechnerherz.example.util

import java.util.*

fun Random.nextInt(min: Int, max: Int): Int = min + nextInt(max - min + 1)

fun Random.nextLong(bound: Int): Long = nextInt(bound).toLong()

fun Random.nextLong(min: Int, max: Int): Long = min + nextLong(max - min + 1)
