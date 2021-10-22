package xyz.izadi.adar.screens.dashboard

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.domain.entity.calculateNetWorth
import xyz.izadi.adar.domain.usecase.AddTransactionsUseCase
import xyz.izadi.adar.domain.usecase.DeleteTransactionUseCase
import xyz.izadi.adar.domain.usecase.FetchAccountWithTransactionsUseCase
import xyz.izadi.adar.domain.usecase.FetchAccountsUseCase
import xyz.izadi.adar.domain.usecase.Result
import xyz.izadi.adar.domain.usecase.runIfSuccess

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val fetchAccountsUseCase: FetchAccountsUseCase,
    private val fetchAccountWithTransactionsUseCase: FetchAccountWithTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val addTransactionsUseCase: AddTransactionsUseCase
) : ViewModel() {
    val accounts: MutableStateFlow<Result<List<Account>>> = MutableStateFlow(Result.Loading)
    val selectedAccountTransactions: MutableStateFlow<Result<AccountWithTransactions>> = MutableStateFlow(Result.Loading)
    val netWorth: MutableStateFlow<Double> = MutableStateFlow(0.0)

    init {
        viewModelScope.launch {
            fetchAccounts()
        }
    }

    @VisibleForTesting
    suspend fun fetchAccounts(updateOnlyIfSuccess: Boolean = false) = withContext(Dispatchers.IO) {
        fetchAccountsUseCase.invoke().collect { result ->
            result.runIfSuccess(updateOnlyIfSuccess) {
                accounts.update { result }
            }

            // if successful -> update the total net worth too
            (result as? Result.Success<List<Account>>)?.state?.let { accounts ->
                netWorth.update { accounts.calculateNetWorth() }
            }
        }
    }

    suspend fun selectAccount(accountId: Int, updateOnlyIfSuccess: Boolean = false) = withContext(Dispatchers.IO) {
        fetchAccountWithTransactionsUseCase.invoke(accountId).collect { result ->
            result.runIfSuccess(updateOnlyIfSuccess) {
                selectedAccountTransactions.update { result }
            }
        }
    }

    fun unselectAccount() {
        selectedAccountTransactions.update { Result.Loading }
    }

    // NOTE: if we delete all items it will think that we have no local data and it will fetch the JSON again
    suspend fun deleteTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        deleteTransactionUseCase.invoke(transaction.id).collect { result ->
            if (result is Result.Success) {
                updateLocalData(transaction.accountId)
            }
        }
    }

    suspend fun restoreTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        addTransactionsUseCase.invoke(listOf(transaction)).collect { result ->
            if (result is Result.Success) {
                updateLocalData(transaction.accountId)
            }
        }
    }

    /*
    * NOTE: this should fetch from internet the new account balance totals... but since we are using a JSON this is not being reflected
    * Similarly, we should also make this calculation locally and show directly to the user while the network call is being processed,
    * but due to time restrictions this functionality hasn't been implemented.
    * */
    private suspend fun updateLocalData(id: Int) {
        selectAccount(id, true)
        fetchAccounts(true)
    }
}
