package xyz.izadi.adar.utils

import java.text.NumberFormat
import java.util.Currency

fun Number.formatCurrency(currencyCode: String): String? = runCatching {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(currencyCode)
    format.format(this@formatCurrency)
}.getOrNull()