package xyz.izadi.adar.screens.dashboard.ui.transactions.sheet

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.izadi.adar.R
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.screens.dashboard.ui.ErrorState
import xyz.izadi.adar.screens.dashboard.ui.LoadingState
import xyz.izadi.adar.screens.dashboard.ui.NoTransactionsState
import xyz.izadi.adar.screens.dashboard.ui.transactions.TransactionListItem
import xyz.izadi.adar.screens.dashboard.ui.transactions.TransactionMonthHeader
import xyz.izadi.adar.ui.components.lists.SwipeToDelete
import xyz.izadi.adar.ui.components.sheet.ExpandableSheetHeader
import xyz.izadi.adar.utils.getYearMonth
import xyz.izadi.adar.utils.isExpandingOrExpanded

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TransactionsSheetContent(
    accountWithTransactions: Result<AccountWithTransactions>,
    sheetState: ModalBottomSheetState,
    onExpandLess: () -> Unit,
    onDismissTransaction: (Transaction) -> Unit
) {
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
                    TransactionsSheetHeader(account = account, transactions = transactions)
                }
            },
            onHide = onExpandLess
        )
        transactions?.let {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                transactions
                    .groupBy { it.date.getYearMonth() }
                    .toSortedMap(compareByDescending { pair -> pair.first + pair.second.value })
                    .forEach { entry ->
                        item {
                            TransactionMonthHeader(
                                year = entry.key.first,
                                month = entry.key.second,
                                currencyCode = account?.currency,
                                totalAmount = entry.value.sumOf { it.amount },
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp)
                            )
                        }
                        items(entry.value.sortedBy { it.date }, { it.id }) { transaction ->
                            SwipeToDelete(onDismiss = { onDismissTransaction(transaction) }) {
                                TransactionListItem(
                                    transaction = transaction,
                                    currencyCode = account?.currency,
                                    modifier = Modifier.background(color = bgColor)
                                )
                            }
                        }
                    }
                if (transactions.isEmpty()) {
                    item {
                        NoTransactionsState()
                    }
                }
            }
        } ?: when (accountWithTransactions) {
            is Result.Loading -> LoadingState()
            else -> ErrorState(stringResource(R.string.tr_error_transactions_message))
        }
    }
}