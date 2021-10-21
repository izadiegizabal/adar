package xyz.izadi.adar.screens.dashboard.ui.accounts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.izadi.adar.R
import xyz.izadi.adar.ui.components.text.Overline
import xyz.izadi.adar.utils.formatCurrency

@ExperimentalAnimationApi
@Composable
fun NetWorthHeader(modifier: Modifier, netWorth: Number) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colors.primary, shape = MaterialTheme.shapes.large)
            .padding(16.dp)
    ) {
        Column {
            Overline(text = stringResource(R.string.db_total_net_worth), color = MaterialTheme.colors.onPrimary)
            AnimatedContent(targetState = netWorth.formatCurrency()) {
                Text(
                    text = it ?: "",
                    style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onPrimary)
                )
            }
        }
    }
}