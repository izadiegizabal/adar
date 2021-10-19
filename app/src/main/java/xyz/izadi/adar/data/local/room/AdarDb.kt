package xyz.izadi.adar.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.data.local.room.dao.AccountsDao

@Database(entities = [AccountImpl::class, TransactionImpl::class], version = 1)
@TypeConverters(Converters::class)
abstract class AdarDb : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao

    companion object {
        const val DB_NAME = "accounts-db"
    }
}