package xyz.izadi.adar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.izadi.adar.data.local.AccountImpl.Companion.TABLE_NAME
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.utils.Constants
import xyz.izadi.adar.utils.formatCurrency

@Entity(tableName = TABLE_NAME)
@Serializable
data class AccountImpl(
    @PrimaryKey
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

    override fun getLocalisedCurrentBalanceInBase(): String? {
        return currentBalanceInBase.formatCurrency(Constants.DEFAULT_CURRENCY_CODE)
    }

    companion object {
        const val TABLE_NAME = "accounts"
    }
}