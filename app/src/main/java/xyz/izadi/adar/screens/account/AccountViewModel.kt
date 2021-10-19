package xyz.izadi.adar.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.domain.usecase.FetchAccountWithTransactionsUseCase

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fetchAccountWithTransactionsUseCase: FetchAccountWithTransactionsUseCase
) : ViewModel() {
    val accountWithTransactions: MutableStateFlow<Result<AccountWithTransactions>> = MutableStateFlow(Result.Loading)

    private var job: Job? = null

    fun updateAccountId(accountId: Int?) {
        if (job?.isActive == true) return

        accountId?.takeIf { doWeNeedToFetch(it) }?.let {
            job = viewModelScope.launch(Dispatchers.IO) {
                fetchAccountWithTransactionsUseCase.invoke(it).collect { result ->
                    accountWithTransactions.update { result }
                }
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