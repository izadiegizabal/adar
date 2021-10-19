package xyz.izadi.adar.screens.account

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.domain.usecase.FetchAccountWithTransactionsUseCase

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fetchAccountWithTransactionsUseCase: FetchAccountWithTransactionsUseCase
) : ViewModel() {
    val accountWithTransactions: MutableStateFlow<Result<AccountWithTransactions>> = MutableStateFlow(Result.Loading)

    suspend fun updateAccountId(accountId: Int?) = withContext(Dispatchers.IO) {
        accountId?.takeIf { doWeNeedToFetch(it) }?.let {
            fetchAccountWithTransactionsUseCase.invoke(it).collect { result ->
                accountWithTransactions.update { result }
            }
        }
    }

    private fun doWeNeedToFetch(accountId: Int) = when (accountWithTransactions.value) {
        is Result.Success<AccountWithTransactions> -> {
            (accountWithTransactions.value as? Result.Success<AccountWithTransactions>)
                ?.state?.account?.id != accountId
        }
        else -> true
    }
}