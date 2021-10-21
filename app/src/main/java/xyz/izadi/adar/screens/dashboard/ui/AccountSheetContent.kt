package xyz.izadi.adar.screens.account

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material.icons.twotone.Savings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.ui.components.SimpleDismissDialog
import xyz.izadi.adar.ui.components.TransactionListItem
import xyz.izadi.adar.ui.components.sheet.ExpandableSheetHeader
import xyz.izadi.adar.ui.components.text.Overline
import xyz.izadi.adar.utils.getCountThisMonth
import xyz.izadi.adar.utils.isExpandingOrExpanded

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AccountSheetContent(accountWithTransactions: Result<AccountWithTransactions>, sheetState: ModalBottomSheetState, onExpandLess: () -> Unit) {
    var shouldShowDialog by remember { mutableStateOf(false) }
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
            handleColor = MaterialTheme.colors.onSurface,
            title = {
                if (account != null && transactions != null) {
                    Column {
                        Overline(text = account.institution)
                        Text(text = account.name, style = MaterialTheme.typography.h6)
                        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 4.dp)) {
                            Text(text = account.getLocalisedCurrentBalance() ?: "", style = MaterialTheme.typography.h4)
                            if (account.currentBalance != account.currentBalanceInBase) {
                                Overline(
                                    text = account.getLocalisedCurrentBalanceInBase()?.let { "(${it})" } ?: "",
                                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .clip(shape = MaterialTheme.shapes.small)
                            .clickable { shouldShowDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.TwoTone.DateRange, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text(text = transactions.getCountThisMonth().toString())
                    }
                }
            },
            onHide = onExpandLess
        )
        (accountWithTransactions as? Result.Success<AccountWithTransactions>)?.state?.let {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(it.transactions) { transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        currencyCode = it.account.currency
                    )
                }
                if (it.transactions.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 96.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.Savings,
                                contentDescription = "",
                                modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(text = "No transactions", modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                }
            }
        } ?: when (accountWithTransactions) {
            is Result.Loading -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 96.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            else -> Text(text = "Error loading transactions: ${(accountWithTransactions as? Result.Error)?.exception?.message}")
        }
    }

    if (shouldShowDialog) {
        SimpleDismissDialog(onDismissRequest = { shouldShowDialog = false }, text = "Number of transactions this month")
    }
}