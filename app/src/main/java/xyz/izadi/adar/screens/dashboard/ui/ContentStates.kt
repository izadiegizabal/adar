package xyz.izadi.adar.screens.dashboard.ui

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
import androidx.compose.ui.platform.testTag
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
            contentDescription = stringResource(R.string.cd_no_transactions_icon),
            modifier = Modifier
                .size(36.dp)
                .testTag("Piggy Icon"),
            tint = MaterialTheme.colors.secondary
        )
        Text(text = stringResource(R.string.tr_no_transactions_message), modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun LoadingState() {
    PaddedContentContainer {
        CircularProgressIndicator(modifier = Modifier.testTag("ProgressIndicator"))
    }
}

@Composable
fun ErrorState(errorMessage: String) {
    PaddedContentContainer {
        Icon(
            imageVector = Icons.TwoTone.Error,
            contentDescription = stringResource(R.string.cd_generic_error_icon),
            modifier = Modifier
                .size(36.dp)
                .testTag("Error Icon"),
            tint = MaterialTheme.colors.secondary
        )
        Text(text = errorMessage, modifier = Modifier.padding(top = 16.dp))
    }
}