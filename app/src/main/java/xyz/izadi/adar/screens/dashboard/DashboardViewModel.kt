package xyz.izadi.adar.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.domain.usecase.FetchAccountsUseCase

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val fetchAccountsUseCase: FetchAccountsUseCase,
) : ViewModel() {
    val accounts: MutableStateFlow<Result<List<Account>>> = MutableStateFlow(Result.Loading)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAccountsUseCase.invoke().collect { result ->
                accounts.update { result }
            }
        }
    }
}