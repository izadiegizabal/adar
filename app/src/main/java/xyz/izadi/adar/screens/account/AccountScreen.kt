package xyz.izadi.adar.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.ui.components.TransactionListItem
import xyz.izadi.adar.utils.goToDashboard

@ExperimentalMaterialApi
@Composable
fun AccountScreen(vm: AccountViewModel = hiltViewModel(), navController: NavController, accountId: Int?, accountName: String?) {

    accountId?.let {
        LaunchedEffect(it) {
            vm.updateAccountId(it)
        }
    }

    val accountWithTransactions by vm.accountWithTransactions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(text = accountName ?: "")
                    (accountWithTransactions as? Result.Success<AccountWithTransactions>)?.state?.account?.let {
                        Text(text = it.getLocalisedCurrentBalance() ?: "")
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.goToDashboard() }) {
                    Icon(imageVector = Icons.TwoTone.ArrowBack, contentDescription = null)
                }
            }
        )

        (accountWithTransactions as? Result.Success<AccountWithTransactions>)?.state?.let {
            val account = it.account
            val transactions = it.transactions
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(transactions) { transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        currencyCode = account.currency
                    )
                }
            }
        } ?: when (accountWithTransactions) {
            is Result.Loading -> CircularProgressIndicator()
            else -> Text(text = "Error loading transactions: ${(accountWithTransactions as? Result.Error)?.exception?.message}")
        }
    }
}