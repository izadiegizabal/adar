package xyz.izadi.adar.utils

import android.content.res.Resources
import androidx.annotation.RawRes
import java.text.NumberFormat
import java.util.Currency
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Number.formatCurrency(currencyCode: String): String? = runCatching {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(currencyCode)
    format.format(this@formatCurrency)
}.getOrNull()

inline fun <reified T> Resources.getObjectFromJson(@RawRes resourceId: Int, json: Json = Json): T {
    return openRawResource(resourceId)
        .bufferedReader()
        .use { it.readText() }.let {
            json.decodeFromString(it)
        }
}