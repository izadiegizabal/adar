package xyz.izadi.adar.data.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.utils.formatCurrency

@Serializable
data class AccountImpl(
    override val id: Int,
    override val name: String,
    override val institution: String,
    override val currency: String,
    @SerialName("current_balance")
    override val currentBalance: Double,
    @SerialName("current_balance_in_base")
    override val currentBalanceInBase: Double
) : Account {
    override fun getLocalisedCurrentBalance(): String? {
        return currentBalance.formatCurrency(currency)
    }
}