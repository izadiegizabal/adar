package xyz.izadi.adar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.datetime.Instant
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.utils.Constants
import xyz.izadi.adar.utils.formatShort

@ExperimentalMaterialApi
@Composable
fun TransactionListItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    currencyCode: String? = Constants.DEFAULT_CURRENCY_CODE
) {
    ListItem(
        modifier = modifier,
        text = { Text(text = transaction.description) },
        secondaryText = { Text(text = transaction.date.formatShort()) },
        trailing = {
            transaction.getLocalisedAmount(currencyCode ?: Constants.DEFAULT_CURRENCY_CODE)?.let {
                Text(text = it)
            }
        }
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun TransactionListItemPreview() {
    Surface {
        TransactionListItem(
            transaction = TransactionImpl(
                id = 22,
                accountId = 2,
                amount = -442.0,
                categoryId = 112,
                date = Instant.parse("2017-05-24T00:00:00+09:00"),
                description = "\u30b9\u30bf\u30fc\u30d0\u30c3\u30af\u30b9 \u539f\u5bbf\u5e97"
            ),
            modifier = Modifier.fillMaxWidth(),
            currencyCode = "JPY"
        )
    }
}