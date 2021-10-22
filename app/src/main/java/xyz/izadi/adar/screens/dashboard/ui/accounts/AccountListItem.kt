package xyz.izadi.adar.screens.dashboard.ui.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ExpandMore
import androidx.compose.material.icons.twotone.Payments
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.izadi.adar.R
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.domain.entity.Account

@ExperimentalMaterialApi
@Composable
fun AccountListItem(modifier: Modifier = Modifier, account: Account) {
    ListItem(
        modifier = modifier,
        text = { Text(text = account.name) },
        secondaryText = {
            account.getLocalisedCurrentBalance()?.let {
                Text(text = it)
            }
        },
        trailing = {
            Icon(imageVector = Icons.TwoTone.ExpandMore, contentDescription = stringResource(R.string.cd_expand_more_icon))
        }
    )
}

@Composable
fun AccountTitle(modifier: Modifier = Modifier, title: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.TwoTone.Payments, contentDescription = stringResource(R.string.cd_account_header_icon), tint = MaterialTheme.colors.secondary)
        Text(
            text = title,
            style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onBackground),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun AccountListItemPreview() {
    Surface {
        Column {
            AccountTitle(title = "Test Bank", modifier = Modifier.padding(start = 16.dp, bottom = 4.dp))
            AccountListItem(
                account = AccountImpl(
                    id = 1,
                    name = "外貨普通(USD)",
                    institution = "Test Bank",
                    currency = "USD",
                    currentBalance = 22.5,
                    currentBalanceInBase = 2306.0
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}