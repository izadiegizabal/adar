package xyz.izadi.adar.screens.dashboard.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import xyz.izadi.adar.R
import xyz.izadi.adar.data.local.TransactionImpl
import xyz.izadi.adar.domain.entity.Transaction
import xyz.izadi.adar.ui.components.text.BoldOverline
import xyz.izadi.adar.utils.Constants
import xyz.izadi.adar.utils.formatCurrency
import xyz.izadi.adar.utils.formatDay

@ExperimentalMaterialApi
@Composable
fun TransactionListItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    currencyCode: String? = Constants.DEFAULT_CURRENCY_CODE
) {
    ListItem(
        modifier = modifier,
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = transaction.date.formatDay(),
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.body2
                )
                Text(text = transaction.description)
            }
        },
        trailing = {
            transaction.getLocalisedAmount(currencyCode ?: Constants.DEFAULT_CURRENCY_CODE)?.let {
                Text(text = it)
            }
        }
    )
}

@Composable
fun TransactionMonthHeader(year: Int, month: Month, currencyCode: String?, totalAmount: Double, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        BoldOverline(text = stringResource(R.string.tr_month_header_date, year, month))
        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier
                .clip(MaterialTheme.shapes.small)
                .background(if (totalAmount > 0) MaterialTheme.colors.primary else MaterialTheme.colors.secondary)
        ) {
            BoldOverline(
                text = totalAmount.formatCurrency(currencyCode).toString(),
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                color = if (totalAmount > 0) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun TransactionListItemPreview() {
    Surface {
        Column {
            TransactionMonthHeader(
                year = 2021,
                month = Month.OCTOBER,
                currencyCode = "JPY",
                totalAmount = -534.0,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
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
}