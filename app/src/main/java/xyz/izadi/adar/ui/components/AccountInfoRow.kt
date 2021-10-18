package xyz.izadi.adar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import xyz.izadi.adar.data.local.AccountImpl
import xyz.izadi.adar.domain.entity.Account

@ExperimentalMaterialApi
@Composable
fun AccountInfoRow(modifier: Modifier = Modifier, account: Account) {
    ListItem(
        modifier = modifier,
        text = { Text(text = account.name) },
        trailing = {
            account.getLocalisedCurrentBalance()?.let {
                Text(text = it)
            }
        }
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun AccountInfoRowPreview() {
    Surface {
        AccountInfoRow(
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