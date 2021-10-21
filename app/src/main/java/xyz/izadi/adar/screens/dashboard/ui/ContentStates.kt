package xyz.izadi.adar.screens.dashboard.ui.transactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material.icons.twotone.Savings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.izadi.adar.R

@Composable
fun PaddedContentContainer(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 96.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun NoTransactionsState() {
    PaddedContentContainer {
        Icon(
            imageVector = Icons.TwoTone.Savings,
            contentDescription = "",
            modifier = Modifier.size(36.dp),
            tint = MaterialTheme.colors.secondary
        )
        Text(text = stringResource(R.string.tr_no_transactions_message), modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun LoadingState() {
    PaddedContentContainer {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(errorMessage: String) {
    PaddedContentContainer {
        Icon(
            imageVector = Icons.TwoTone.Error,
            contentDescription = "",
            modifier = Modifier.size(36.dp),
            tint = MaterialTheme.colors.secondary
        )
        Text(text = errorMessage, modifier = Modifier.padding(top = 16.dp))
    }
}