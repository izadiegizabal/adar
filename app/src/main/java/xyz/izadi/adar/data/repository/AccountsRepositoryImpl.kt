package xyz.izadi.adar.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import xyz.izadi.adar.R
import xyz.izadi.adar.data.local.room.dao.AccountsDao
import xyz.izadi.adar.data.network.AccountsResponse
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.domain.repository.AccountsRepository

class AccountsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountsDao: AccountsDao
) : AccountsRepository {
    override suspend fun getAccounts(): List<Account> {
        // TODO: set up a validity rule or some cache policy to know when to perform external call vs load local copy
        if (!accountsDao.doWeHaveAccounts()) {
            // TODO: replace this with a network call to fetch it from the internet
            val accountsJsonString = context.resources
                .openRawResource(R.raw.accounts)
                .bufferedReader()
                .use { it.readText() }
            val accounts = Json.decodeFromString<AccountsResponse>(accountsJsonString).accounts
            accountsDao.saveAccounts(accounts = accounts)
        }
        return accountsDao.getAccounts()
    }

    override suspend fun getTransactions(): List<Transaction> {
        TODO("Not yet implemented")
    }

}
