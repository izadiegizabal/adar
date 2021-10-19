package xyz.izadi.adar.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.usecase.FetchAccountsUseCase
import xyz.izadi.adar.domain.usecase.Result

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val fetchAccountsUseCase: FetchAccountsUseCase
) : ViewModel() {

    private val _accounts: MutableStateFlow<Result<List<Account>>> = MutableStateFlow(Result.Loading)
    val accounts: StateFlow<Result<List<Account>>>
        get() = _accounts

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAccountsUseCase.invoke().collect { result ->
                _accounts.update { result }
            }
        }
    }
}