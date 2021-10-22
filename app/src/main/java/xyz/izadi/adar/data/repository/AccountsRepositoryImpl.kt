package xyz.izadi.adar.data.repository

import javax.inject.Inject
import xyz.izadi.adar.R
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.data.local.room.dao.AccountsDao
import xyz.izadi.adar.data.local.room.dao.TransactionsDao
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.domain.repository.AccountsRepository

// TODO: set up a validity rule or some cache policy to know when to perform external call vs load local copy
// TODO: replace local json reading calls with network API calls
class AccountsRepositoryImpl @Inject constructor(
    private val accountsDao: AccountsDao,
    private val transactionsDao: TransactionsDao,
    private val dataLoader: DataLoader
) : AccountsRepository {
    override suspend fun getAccounts(): List<Account> {
        if (!accountsDao.doWeHaveAccounts()) {
            val accounts = dataLoader.fetchAccounts()
            accountsDao.saveAccounts(accounts = accounts)
        }
        return accountsDao.getAccounts()
    }

    override suspend fun getAccountWithTransactions(accountId: Int): AccountWithTransactions {
        if (!transactionsDao.doWeHaveTransactionsForAccount(accountId = accountId)) {
            val transactions = dataLoader.getTransactions(accountId)
            transactionsDao.saveTransactions(transactions)
        }
        return accountsDao.getAccountWithTransactions(accountId)
    }

    override suspend fun deleteTransaction(transactionId: Int) {
        return transactionsDao.delete(transactionId)
    }

    override suspend fun addTransactions(transactions: List<Transaction>) {
        val transImpl = transactions.map {
            TransactionImpl(it.accountId, it.amount, it.categoryId, it.date, it.description, it.id)
        }.toList()
        return transactionsDao.saveTransactions(transImpl)
    }


}
