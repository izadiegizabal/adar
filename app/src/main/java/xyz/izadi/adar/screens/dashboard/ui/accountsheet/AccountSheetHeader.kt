package xyz.izadi.adar.screens.dashboard.ui.accountsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.izadi.adar.R
import xyz.izadi.adar.domain.entity.Account
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.ui.components.dialogs.SimpleDismissDialog
import xyz.izadi.adar.ui.components.text.Overline
import xyz.izadi.adar.utils.getCountThisMonth

@Composable
fun ColumnScope.AccountSheetHeader(account: Account, transactions: List<Transaction>) {
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
    TransactionCounter(count = transactions.getCountThisMonth())
}

@Composable
fun TransactionCounter(count: Int) {
    var shouldShowDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.small)
            .clickable { shouldShowDialog = true }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.TwoTone.DateRange, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
        Text(text = count.toString())
    }

    if (shouldShowDialog) {
        SimpleDismissDialog(
            onDismissRequest = { shouldShowDialog = false },
            text = stringResource(R.string.dialog_monthly_transaction_count_explanation)
        )
    }
}