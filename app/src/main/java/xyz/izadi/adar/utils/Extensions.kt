package xyz.izadi.adar.utils

import android.content.res.Resources
import androidx.annotation.RawRes
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.DateFormat.SHORT
import java.text.DateFormat.getDateInstance
import java.text.NumberFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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

fun Instant.formatShort(): String = runCatching {
    getDateInstance(SHORT, Locale.getDefault()).format(Date.from(this.toJavaInstant()))
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