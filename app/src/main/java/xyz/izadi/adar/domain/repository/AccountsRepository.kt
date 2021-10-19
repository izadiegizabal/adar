package xyz.izadi.adar.domain.repository

import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.AccountWithTransactions

interface AccountsRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun getAccountWithTransactions(accountId: Int): AccountWithTransactions
}