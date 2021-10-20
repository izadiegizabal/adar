package xyz.izadi.adar.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Payments
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.ui.components.AccountListItem
import xyz.izadi.adar.ui.components.Base
import xyz.izadi.adar.utils.formatCurrency
import xyz.izadi.adar.utils.goToAccount

@ExperimentalMaterialApi
@Composable
fun DashboardScreen(vm: DashboardViewModel = hiltViewModel(), navController: NavController) {
    val accounts by vm.accounts.collectAsState()
    val netWorth by vm.netWorth.collectAsState()

    Base {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
                .background(MaterialTheme.colors.primary, shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Column {
                Text(text = "NET WORTH", style = MaterialTheme.typography.overline)
                Text(
                    text = "${netWorth.formatCurrency()}",
                    style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground)
                )
            }
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            when (accounts) {
                is Result.Success<List<Account>> -> (accounts as? Result.Success<List<Account>>)?.state?.let { accounts ->
                    item {
                        Text(
                            text = "ACCOUNTS",
                            style = MaterialTheme.typography.overline,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    accounts.sortedBy { it.institution }.groupBy { it.institution }.forEach {
                        item {
                            Row(
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.TwoTone.Payments, contentDescription = null)
                                Text(
                                    text = it.key,
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        items(it.value.sortedBy { account -> account.name }) { account ->
                            AccountListItem(
                                account = account,
                                modifier = Modifier.clickable {
                                    navController.goToAccount(id = account.id, name = account.name)
                                }
                            )
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
}

