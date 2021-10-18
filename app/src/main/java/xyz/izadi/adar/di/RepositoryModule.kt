package xyz.izadi.adar.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.izadi.adar.data.repository.AccountsRepositoryImpl
import xyz.izadi.adar.domain.repository.AccountsRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAccountsRepository(impl: AccountsRepositoryImpl): AccountsRepository
}