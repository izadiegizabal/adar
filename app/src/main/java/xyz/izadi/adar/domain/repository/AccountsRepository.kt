package xyz.izadi.adar.domain.repository

import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Transaction

interface AccountsRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun getAccountWithTransactions(accountId: Int): AccountWithTransactions
    suspend fun deleteTransaction(transactionId: Int)
    suspend fun addTransactions(transactions: List<Transaction>)
}