package xyz.izadi.adar.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import xyz.izadi.adar.R
import xyz.izadi.adar.data.local.room.dao.AccountsDao
import xyz.izadi.adar.data.local.room.dao.TransactionsDao
import xyz.izadi.adar.data.network.AccountsResponse
import xyz.izadi.adar.data.network.TransactionsResponse
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.repository.AccountsRepository
import xyz.izadi.adar.utils.getObjectFromJson

// TODO: set up a validity rule or some cache policy to know when to perform external call vs load local copy
// TODO: replace local json reading calls with network API calls
class AccountsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountsDao: AccountsDao,
    private val transactionsDao: TransactionsDao
) : AccountsRepository {
    override suspend fun getAccounts(): List<Account> {
        if (!accountsDao.doWeHaveAccounts()) {
            val accounts = context.resources.getObjectFromJson<AccountsResponse>(R.raw.accounts).accounts
            accountsDao.saveAccounts(accounts = accounts)
        }
        return accountsDao.getAccounts()
    }

    override suspend fun getAccountWithTransactions(accountId: Int): AccountWithTransactions {
        if (!transactionsDao.doWeHaveTransactionsForAccount(accountId = accountId)) {
            val transactions = context.resources.getObjectFromJson<TransactionsResponse>(
                resourceId = when (accountId) {
                    1 -> R.raw.transactions_1
                    2 -> R.raw.transactions_2
                    3 -> R.raw.transactions_3
                    else -> throw Exception("No transactions for this") // TODO: create proper exception
                }
            ).transactions
            transactionsDao.saveTransactions(transactions)
        }
        return accountsDao.getAccountWithTransactions(accountId)
    }

}
