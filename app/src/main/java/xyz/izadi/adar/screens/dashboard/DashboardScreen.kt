package xyz.izadi.adar.screens.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Payments
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import xyz.izadi.adar.R
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.screens.account.AccountSheetContent
import xyz.izadi.adar.ui.components.AccountListItem
import xyz.izadi.adar.ui.components.Base
import xyz.izadi.adar.ui.components.sheet.BottomSheet
import xyz.izadi.adar.ui.components.text.Overline
import xyz.izadi.adar.utils.formatCurrency

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DashboardScreen(vm: DashboardViewModel = hiltViewModel()) {
    val accounts by vm.accounts.collectAsState()
    val netWorth by vm.netWorth.collectAsState()
    val selectedAccountTransactions by vm.selectedAccountTransactions.collectAsState()

    val accountSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    BottomSheet(
        sheetState = accountSheetState,
        scope = scope,
        onHide = { vm.unselectAccount() },
        id = selectedAccountTransactions,
        sheetContent = {
            AccountSheetContent(
                accountWithTransactions = selectedAccountTransactions,
                sheetState = accountSheetState,
                onExpandLess = {
                    scope.launch {
                        accountSheetState.hide()
                    }
                }
            )
        }
    ) {
        Base {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp)
                    .background(MaterialTheme.colors.primary, shape = MaterialTheme.shapes.large)
                    .padding(16.dp)
            ) {
                Column {
                    Overline(text = stringResource(R.string.db_total_net_worth))
                    AnimatedContent(targetState = netWorth.formatCurrency()) {
                        Text(
                            text = it ?: "",
                            style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground)
                        )
                    }
                }
            }
            AnimatedContent(targetState = accounts) { accounts ->
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    when (accounts) {
                        is Result.Success<List<Account>> -> (accounts as? Result.Success<List<Account>>)?.state?.let { accounts ->
                            item {
                                Overline(text = stringResource(R.string.db_accounts_title), modifier = Modifier.padding(horizontal = 16.dp))
                            }
                            accounts.groupBy { it.institution }.forEach {
                                item {
                                    Row(
                                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.TwoTone.Payments, contentDescription = null, tint = MaterialTheme.colors.secondary)
                                        Text(
                                            text = it.key,
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                                items(it.value) { account ->
                                    AccountListItem(
                                        account = account,
                                        modifier = Modifier.clickable {
                                            scope.launch {
                                                accountSheetState.show()
                                                vm.selectAccount(account.id)
                                            }
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
    }

}

