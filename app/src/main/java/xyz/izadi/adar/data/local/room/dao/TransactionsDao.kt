package xyz.izadi.adar.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xyz.izadi.adar.data.local.TransactionImpl

@Dao
interface TransactionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTransactions(transactions: List<TransactionImpl>)

    @Query("SELECT * FROM ${TransactionImpl.TABLE_NAME} WHERE accountId = :accountId ORDER BY date")
    fun getTransactionsForAccount(accountId: Int): List<TransactionImpl>

    @Query("SELECT EXISTS(SELECT * FROM ${TransactionImpl.TABLE_NAME} WHERE accountId = :accountId)")
    fun doWeHaveTransactionsForAccount(accountId: Int): Boolean
}