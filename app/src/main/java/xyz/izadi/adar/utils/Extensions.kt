package xyz.izadi.adar.utils

import android.content.res.Resources
import androidx.annotation.RawRes
import java.text.DateFormat.SHORT
import java.text.DateFormat.getDateInstance
import java.text.NumberFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Number.formatCurrency(currencyCode: String? = null): String? = runCatching {

    val format = NumberFormat.getCurrencyInstance()
    format.currency = when {
        currencyCode != null -> Currency.getInstance(currencyCode)
        else -> Currency.getInstance(Locale.getDefault()) // let's assume that the base currency is the devices locale one
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