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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// TODO: move these extensions into more meaningful places

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

@ExperimentalMaterialApi
fun ModalBottomSheetState.isExpandingOrExpanded() = this.targetValue == ModalBottomSheetValue.Expanded

fun Int.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).toInt().dp