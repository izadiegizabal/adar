package xyz.izadi.adar.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import xyz.izadi.adar.R
import xyz.izadi.adar.data.network.AccountsResponse
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.domain.repository.AccountsRepository

class AccountsRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : AccountsRepository {
    override suspend fun getAccounts(): List<Account> {
        // TODO: replace this with a network call to fetch it from the internet
        val accountsJsonString = context.resources
            .openRawResource(R.raw.accounts)
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString<AccountsResponse>(accountsJsonString).accounts
    }

    override suspend fun getTransactions(): List<Transaction> {
        TODO("Not yet implemented")
    }

}
