package xyz.izadi.adar.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.ui.components.AccountInfoRow
import xyz.izadi.adar.utils.goToDashboard

@ExperimentalMaterialApi
@Composable
fun AccountScreen(vm: AccountViewModel = hiltViewModel(), navController: NavController, accountId: Int?) {

    accountId?.let {
        LaunchedEffect(it) {
            vm.updateAccountId(it)
        }
    }

    val accountWithTransactions by vm.accountWithTransactions.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column {
                Text(text = "This is the account $accountId screen!")
                Button(onClick = { navController.goToDashboard() }) {
                    Text(text = "Go To Dashboard")
                }
            }
        }
        when (accountWithTransactions) {
            is Result.Success<AccountWithTransactions> -> (accountWithTransactions as? Result.Success<AccountWithTransactions>)?.state?.let { accountWithTransactions ->
                item {
                    AccountInfoRow(account = accountWithTransactions.account)
                }
                items(accountWithTransactions.transactions) { transaction ->
                    Text(text = "${transaction.description} - ${transaction.amount}")
                }
            }
            is Result.Loading -> item {
                CircularProgressIndicator()
            }
            else -> item {
                Text(text = "Error loading transactions: ${(accountWithTransactions as? Result.Error)?.exception?.message}")
            }
        }
    }
}