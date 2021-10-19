package xyz.izadi.adar.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.ui.components.AccountInfoRow
import xyz.izadi.adar.utils.goToAccount

@ExperimentalMaterialApi
@Composable
fun DashboardScreen(vm: DashboardViewModel = hiltViewModel(), navController: NavController) {
    val accounts by vm.accounts.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        when (accounts) {
            is Result.Success<List<Account>> -> (accounts as? Result.Success<List<Account>>)?.state?.let { accounts ->
                accounts.sortedBy { it.institution }.groupBy { it.institution }.forEach {
                    item {
                        Text(text = it.key, style = MaterialTheme.typography.h5)
                    }
                    items(it.value.sortedBy { account -> account.name }) { account ->
                        AccountInfoRow(account = account, modifier = Modifier.clickable {
                            navController.goToAccount(id = account.id)
                        })
                    }
                }
            }
            is Result.Loading -> item {
                CircularProgressIndicator()
            }
            else -> item {
                Text(text = "Error loading accounts: ${(accounts as? Result.Error)?.exception?.message}")
            }
        }
    }
}

