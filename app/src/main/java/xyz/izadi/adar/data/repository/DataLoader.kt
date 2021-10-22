package xyz.izadi.adar.data.repository

import android.content.Context
import androidx.annotation.RawRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import xyz.izadi.adar.R
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.data.network.AccountsResponse
import xyz.izadi.adar.data.network.TransactionsResponse
import xyz.izadi.adar.utils.getObjectFromJson

class DataLoader @Inject constructor(
    @ApplicationContext context: Context
) {
    private val resources by lazy { context.resources }

    fun fetchAccounts(): List<AccountImpl> {
        return resources.getObjectFromJson<AccountsResponse>(R.raw.accounts).accounts
    }

    fun getTransactions(@RawRes resId: Int): List<TransactionImpl> {
        return resources.getObjectFromJson<TransactionsResponse>(resId).transactions
    }
}