package xyz.izadi.adar.utils

import android.content.res.Resources
import androidx.annotation.RawRes
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Currency
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.domain.entity.Transaction

fun Number.formatCurrency(currencyCode: String? = null): String? = runCatching {

    val format = NumberFormat.getCurrencyInstance()
    format.currency = when {
        currencyCode != null -> Currency.getInstance(currencyCode)
        else -> Currency.getInstance(Constants.DEFAULT_CURRENCY_CODE) // let's assume that the base currency is the japanese yen, we could also take the locale one with Locale.getDefault()
    }

    format.format(this@formatCurrency)
}.getOrNull()

inline fun <reified T> Resources.getObjectFromJson(@RawRes resourceId: Int, json: Json = Json): T {
    return openRawResource(resourceId)
        .bufferedReader()
        .use { it.readText() }.let {
            json.decodeFromString(it)
        }
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

@ExperimentalMaterialApi
fun ModalBottomSheetState.isExpandingOrExpanded() = this.targetValue == ModalBottomSheetValue.Expanded

fun Int.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).toInt().dp

fun Instant.isCurrentMonth(): Boolean {
    val given = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val current = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return given.year == current.year &&
            given.month == current.month
}

fun List<Transaction>.getCountThisMonth(): Int = filter { trans -> trans.date.isCurrentMonth() }.size

fun Instant.getYearMonth(): Pair<Int, Month> {
    val given = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return Pair(given.year, given.month)
}

fun Result<*>.runIfSuccess(enabled: Boolean = true, run: () -> Unit) {
    if (enabled && this is Result.Success) {
        run()
    } else if (!enabled) {
        run()
    }
}