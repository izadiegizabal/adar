package xyz.izadi.adar.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import xyz.izadi.adar.data.local.AccountImpl

@Dao
interface AccountsDao {
    @Insert(onConflict = REPLACE)
    fun saveAccounts(accounts: List<AccountImpl>)

    @Query("SELECT * FROM ${AccountImpl.TABLE_NAME}")
    fun getAccounts(): List<AccountImpl>

    @Query("SELECT EXISTS(SELECT * FROM ${AccountImpl.TABLE_NAME})")
    fun doWeHaveAccounts(): Boolean
}