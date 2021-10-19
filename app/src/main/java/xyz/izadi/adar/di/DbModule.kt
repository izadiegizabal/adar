package xyz.izadi.adar.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import xyz.izadi.adar.data.local.room.AdarDb

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Singleton
    @Provides
    fun providesDb(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        AdarDb::class.java,
        AdarDb.DB_NAME
    ).build()

    @Singleton
    @Provides
    fun providesAccountsDao(db: AdarDb) = db.accountsDao()

    @Singleton
    @Provides
    fun providesTransactionsDao(db: AdarDb) = db.transactionsDao()
}