package xyz.izadi.adar.screens.account

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.screens.dashboard.ui.AccountSheetHeader
import xyz.izadi.adar.screens.dashboard.ui.ErrorState
import xyz.izadi.adar.screens.dashboard.ui.LoadingState
import xyz.izadi.adar.screens.dashboard.ui.NoTransactionsState
import xyz.izadi.adar.ui.components.TransactionListItem
import xyz.izadi.adar.ui.components.sheet.ExpandableSheetHeader
import xyz.izadi.adar.utils.isExpandingOrExpanded

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AccountSheetContent(accountWithTransactions: Result<AccountWithTransactions>, sheetState: ModalBottomSheetState, onExpandLess: () -> Unit) {
    val bgColor: Color by animateColorAsState(targetValue = if (sheetState.isExpandingOrExpanded()) MaterialTheme.colors.background else MaterialTheme.colors.surface)

    val account = (accountWithTransactions as? Result.Success<AccountWithTransactions>)?.state?.account
    val transactions = (accountWithTransactions as? Result.Success<AccountWithTransactions>)?.state?.transactions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        ExpandableSheetHeader(
            sheetState = sheetState,
            title = {
                if (account != null && transactions != null) {
                    AccountSheetHeader(account = account, transactions = transactions)
                }
            },
            onHide = onExpandLess
        )
        transactions?.let {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(transactions) { transaction ->
                    TransactionListItem(transaction = transaction, currencyCode = account?.currency)
                }
                if (transactions.isEmpty()) {
                    item {
                        NoTransactionsState()
                    }
                }
            }
        } ?: when (accountWithTransactions) {
            is Result.Loading -> LoadingState()
            else -> ErrorState()
        }
    }
}