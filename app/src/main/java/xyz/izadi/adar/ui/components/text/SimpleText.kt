package xyz.izadi.adar.ui.components.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase

@Composable
fun Overline(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.toUpperCase(Locale.current),
        style = MaterialTheme.typography.overline,
        modifier = modifier
    )
}