package xyz.izadi.adar.screens.account

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.twotone.Savings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.ui.components.Base
import xyz.izadi.adar.ui.components.TransactionListItem
import xyz.izadi.adar.ui.components.sheet.ExpandableSheetHeader

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AccountSheetContent(accountWithTransactions: Result<AccountWithTransactions>, sheetState: ModalBottomSheetState, onExpandLess: () -> Unit) {

    val account = (accountWithTransactions as? Result.Success<AccountWithTransactions>)?.state?.account

    Base {
        ExpandableSheetHeader(
            sheetState = sheetState,
            bgColor = MaterialTheme.colors.primary,
            handleColor = MaterialTheme.colors.onPrimary,
            title = {
                Text(text = account?.name ?: "")
                Text(text = account?.getLocalisedCurrentBalance() ?: "")
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
}