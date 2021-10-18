package xyz.izadi.adar.domain.entity

interface Account {
    val id: Int
    val name: String
    val institution: String
    val currency: String
    val currentBalance: Double
    val currentBalanceInBase: Double

    fun getLocalisedCurrentBalance(): String?
}