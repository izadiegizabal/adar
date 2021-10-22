package xyz.izadi.adar.domain.entity

import java.io.Serializable
import kotlinx.datetime.Instant

interface Transaction : Serializable {
    val accountId: Int
    val amount: Double
    val categoryId: Int
    val date: Instant
    val description: String
    val id: Int

    fun getLocalisedAmount(currencyCode: String): String?
}

fun List<Transaction>.getCountThisMonth(): Int = filter { trans -> trans.date.isCurrentMonth() }.size