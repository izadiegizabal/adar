package xyz.izadi.adar.domain.entity

import kotlinx.datetime.Instant

interface Transaction {
    val accountId: Int
    val amount: Double
    val categoryId: Int
    val date: Instant
    val description: String
    val id: Int
}