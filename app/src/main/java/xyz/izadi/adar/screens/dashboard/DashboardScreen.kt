package xyz.izadi.adar.screens.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import xyz.izadi.adar.R
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.screens.dashboard.ui.ErrorState
import xyz.izadi.adar.screens.dashboard.ui.LoadingState
import xyz.izadi.adar.screens.dashboard.ui.accounts.AccountListItem
import xyz.izadi.adar.screens.dashboard.ui.accounts.AccountTitle
import xyz.izadi.adar.screens.dashboard.ui.accounts.NetWorthHeader
import xyz.izadi.adar.screens.dashboard.ui.transactions.sheet.TransactionsSheetContent
import xyz.izadi.adar.ui.components.layouts.Base
import xyz.izadi.adar.ui.components.sheet.BottomSheet
import xyz.izadi.adar.ui.components.text.Overline

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DashboardScreen(vm: DashboardViewModel = hiltViewModel()) {
    val accounts by vm.accounts.collectAsState()
    val netWorth by vm.netWorth.collectAsState()
    val selectedAccountTransactions by vm.selectedAccountTransactions.collectAsState()

    val accountSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        BottomSheet(
            sheetState = accountSheetState,
            scope = scope,
            onHide = { vm.unselectAccount() },
            id = selectedAccountTransactions,
            sheetContent = {
                TransactionsSheetContent(
                    accountWithTransactions = selectedAccountTransactions,
                    sheetState = accountSheetState,
                    onExpandLess = {
                        scope.launch {
                            accountSheetState.hide()
                        }
                    },
                    onDismissTransaction = {
                        scope.launch {
                            vm.deleteTransaction(it)
                            val result = scaffoldState.snackbarHostState.showSnackbar(message = "Transaction deleted", actionLabel = "Restore")
                            if (result == SnackbarResult.ActionPerformed) {
                                vm.restoreTransaction(it)
                            }
                        }
                    }
                )
            }
        ) {
            Base {
                NetWorthHeader(
                    netWorth = netWorth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                )
                AnimatedContent(targetState = accounts) { accounts ->
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        (accounts as? Result.Success<List<Account>>)?.state?.let { accounts ->
                            item {
                                Overline(text = stringResource(R.string.db_accounts_title), modifier = Modifier.padding(horizontal = 16.dp))
                            }
                            accounts.groupBy { it.institution }.forEach {
                                item {
                                    AccountTitle(
                                        title = it.key,
                                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
                                    )
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
                        } ?: item {
                            when (accounts) {
                                is Result.Loading -> LoadingState()
                                else -> ErrorState(stringResource(R.string.db_loading_error))
                            }
                        }
                    }
                }
            }
        }
    }
}

