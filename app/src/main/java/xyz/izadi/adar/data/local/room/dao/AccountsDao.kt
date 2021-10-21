package xyz.izadi.adar.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.local.AccountWithTransactionsImpl

@Dao
interface AccountsDao {
    @Insert(onConflict = REPLACE)
    fun saveAccounts(accounts: List<AccountImpl>)

    @Query("SELECT * FROM ${AccountImpl.TABLE_NAME} ORDER BY institution, name")
    fun getAccounts(): List<AccountImpl>

    @Transaction
    @Query("SELECT * FROM ${AccountImpl.TABLE_NAME} WHERE id = :accountId")
    fun getAccountWithTransactions(accountId: Int): AccountWithTransactionsImpl

    @Query("SELECT EXISTS(SELECT * FROM ${AccountImpl.TABLE_NAME})")
    fun doWeHaveAccounts(): Boolean
}