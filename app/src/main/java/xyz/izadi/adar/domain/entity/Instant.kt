package xyz.izadi.adar.domain.entity

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.isCurrentMonth(): Boolean {
    val given = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val current = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return given.year == current.year &&
            given.month == current.month
}

fun Instant.getYearMonth(): Pair<Int, Month> {
    val given = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return Pair(given.year, given.month)
}

// Note: this is not ideal for internalization and should be checked had I have more time
fun Instant.formatDay(): String = runCatching {
    val dayOfMonth = this.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfMonth

    return when {
        listOf(1, 21, 31).any { it == dayOfMonth } -> "${dayOfMonth}st"
        listOf(2, 22).any { it == dayOfMonth } -> "${dayOfMonth}nd"
        listOf(3, 23).any { it == dayOfMonth } -> "${dayOfMonth}rd"
        else -> "${dayOfMonth}th"
    }
}.getOrDefault("")