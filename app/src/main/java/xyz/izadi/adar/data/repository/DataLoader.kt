package xyz.izadi.adar.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.jvm.Throws
import xyz.izadi.adar.R
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.data.network.AccountsResponse
import xyz.izadi.adar.data.network.TransactionsResponse
import xyz.izadi.adar.domain.entity.NoTransactionsException
import xyz.izadi.adar.utils.getObjectFromJson

class DataLoader @Inject constructor(
    @ApplicationContext context: Context
) {
    private val resources by lazy { context.resources }

    fun fetchAccounts(): List<AccountImpl> {
        return resources.getObjectFromJson<AccountsResponse>(R.raw.accounts).accounts
    }

    @Throws(NoTransactionsException::class)
    fun getTransactions(accountId: Int): List<TransactionImpl> {
        val resId = when (accountId) {
            1 -> R.raw.transactions_1
            2 -> R.raw.transactions_2
            3 -> R.raw.transactions_3
            else -> throw NoTransactionsException()
        }
        return resources.getObjectFromJson<TransactionsResponse>(resId).transactions
    }
}