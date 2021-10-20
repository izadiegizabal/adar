package xyz.izadi.adar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
            Icon(imageVector = Icons.TwoTone.ExpandMore, contentDescription = "")
        }
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun AccountListItemPreview() {
    Surface {
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